package dev.newsletterss.api.filter;

import dev.newsletterss.api.service.JwtTokenUtilImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 인증용 필터
 * @author 이상일
 * @version 1.0
 * (2020.02.11) 이상일, 최초 작성
 */
@Slf4j
public class jwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private JwtTokenUtilImpl jwtTokenUtilImpl;


	private final AuthenticationManager authenticationManager;

	public jwtAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
		this.authenticationManager = authenticationManager;
		setFilterProcessesUrl("/auth/**");

		jwtTokenUtilImpl = ctx.getBean(JwtTokenUtilImpl.class);
	}
	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
												HttpServletResponse httpServletResponse) throws AuthenticationException {
		ReadableRequestWrapper wrapper = new ReadableRequestWrapper(httpServletRequest);
		String username = wrapper.getParameter("username");
		String rawPw = wrapper.getParameter("password");
		UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(username, rawPw);

		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    // System.out.println("?? : " +failed.ge);

		super.unsuccessfulAuthentication(request, response, failed);
	}

	@Override
	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {

		super.setAuthenticationFailureHandler(failureHandler);
	}

	@Override
	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		super.setAuthenticationSuccessHandler(successHandler);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
	/*	User autnenticatedUser = ((User)authResult.getPrincipal());
		ArrayList userrole = (ArrayList) autnenticatedUser.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());*/
		User authenticatedUser = ((User)authResult.getPrincipal());
			String authusernm = authenticatedUser.getUsername();
			System.out.println(authusernm);
			String accToken = null;
			log.info("sss"+(jwtTokenUtilImpl == null));
			try {
				accToken = jwtTokenUtilImpl.createJwtToken(authusernm);
				String refToken = jwtTokenUtilImpl.createJwtRefreshToken(authenticatedUser.getUsername());
				response.addHeader("accessToken", accToken);
				response.addHeader("refreshToken", refToken);
				SecurityContext context = SecurityContextHolder.createEmptyContext();
			    context.setAuthentication(authResult);
			    SecurityContextHolder.setContext(context);
			    chain.doFilter(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public class ReadableRequestWrapper extends HttpServletRequestWrapper { // 상속
		private final Charset encoding;
		private byte[] rawData;
		private Map<String, String[]> params = new HashMap<>();

		public ReadableRequestWrapper(HttpServletRequest request) {
			super(request);
			this.params.putAll(request.getParameterMap()); // 원래의 파라미터를 저장

			String charEncoding = request.getCharacterEncoding(); // 인코딩 설정
			this.encoding = StringUtils.isEmpty(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

			try {
				InputStream is = request.getInputStream();
				this.rawData = IOUtils.toByteArray(is); // InputStream 을 별도로 저장한 다음 getReader() 에서 새 스트림으로 생성

				// body 파싱
				String collect = this.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				if (StringUtils.isEmpty(collect)) { // body 가 없을경우 로깅 제외
					return;
				}
            /*if (request.getContentType() != null && request.getContentType().contains(
                    ContentType.MULTIPART_FORM_DATA.getMimeType())) { // 파일 업로드시 로깅제외
                return;
            }*/
				JSONParser jsonParser = new JSONParser();
				Object parse = jsonParser.parse(collect);
				if (parse instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) ((JSONParser) jsonParser).parse(collect);
					setParameter("requestBody", jsonArray.toString());
				} else {
					JSONObject jsonObject = (JSONObject)jsonParser.parse(collect);
					Iterator iterator = jsonObject.keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String)iterator.next();
						setParameter(key, jsonObject.get(key).toString().replace("\"", "\\\""));
					}
				}
			} catch (Exception e) {
//            log.error("ReadableRequestWrapper init error", e);
			}
		}

		@Override
		public String getParameter(String name) {
			String[] paramArray = getParameterValues(name);
			if (paramArray != null && paramArray.length > 0) {
				return paramArray[0];
			} else {
				return null;
			}
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			return Collections.unmodifiableMap(params);
		}

		@Override
		public Enumeration<String> getParameterNames() {
			return Collections.enumeration(params.keySet());
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] result = null;
			String[] dummyParamValue = params.get(name);

			if (dummyParamValue != null) {
				result = new String[dummyParamValue.length];
				System.arraycopy(dummyParamValue, 0, result, 0, dummyParamValue.length);
			}
			return result;
		}

		public void setParameter(String name, String value) {
			String[] param = {value};
			setParameter(name, param);
		}

		public void setParameter(String name, String[] values) {
			params.put(name, values);
		}

		@Override
		public ServletInputStream getInputStream() {
			final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

			return new ServletInputStream() {
				@Override
				public boolean isFinished() {
					return false;
				}

				@Override
				public boolean isReady() {
					return false;
				}

				@Override
				public void setReadListener(ReadListener readListener) {
					// Do nothing
				}

				public int read() {
					return byteArrayInputStream.read();
				}
			};
		}

		@Override
		public BufferedReader getReader() {
			return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
		}
	}
}
