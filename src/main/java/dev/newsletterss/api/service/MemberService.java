package dev.newsletterss.api.service;
/**
 * 사용자 회원가입, 로그인, 정보 수정, 탈퇴 서비스
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import dev.newsletterss.api.dto.MemberRequestDTO;
import dev.newsletterss.api.entity.Member;
import dev.newsletterss.api.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/*
	 * 회원가입
	 *
	 * @ param MemberRequestDTO memberRequestDto entity 데이터를 담은 객체
	 * @ return Member Entity
	 */
	@Transactional
	public Member joinMember(MemberRequestDTO memberRequestDto) {

		String username = memberRequestDto.toEntity().getUsername();
		String encodedPassword = passwordEncoder.encode(memberRequestDto.toEntity().getPassword());
		LocalDateTime currentTime = LocalDateTime.now();
		Member newMember = Member.builder()
				.username(username)
				.password(encodedPassword)
				.email(memberRequestDto.toEntity().getEmail())
				.regdate(currentTime)
				.userrole("MEMBER")
				.build();
		return memberRepository.save(newMember);
	}

	@Transactional
	public void loginMember(MemberRequestDTO memberRequestDto) {

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> memberEntityWrapper = memberRepository.findByUsername(username);
		Member memberEntity = memberEntityWrapper.get();
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (memberEntity.getUsername().equals("admin")) {
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
		} else {
			authorities.add(new SimpleGrantedAuthority("MEMBER"));
		}

		User user = new User(memberEntity.getUsername(), memberEntity.getPassword(), authorities);
		System.out.println("paw : " + user.getPassword());
		return user;
	}

}