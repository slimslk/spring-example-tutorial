package com.dimm.springbootexample.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtUtil {

	private static final byte[] KEY = "cghbyuntcn_l;fcn_ajh_kthybyu-cghbyu-ghbvth-njkmrj-lkz-bpextybz_93357919735-'nj-ghjcnj_yjvthf".getBytes();

	public String issueToken(String subject, String ...scopes) {
		return issueToken(subject, Map.of("scopes", scopes));
	}

	public String issueToken(String subject, List<String> scopes) {
		return issueToken(subject, Map.of("scopes", scopes));
	}

	public String issueToken(String subject) {
		return issueToken(subject, Map.of());
	}

	public String issueToken(String subject, Map<String, Object> claims) {
		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuer("https://dimm.com")
				.issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plus(
						2, ChronoUnit.DAYS)
				))
				.signWith(getSignedKey())
				.compact();
	}
	private SecretKey getSignedKey () {
		return Keys.hmacShaKeyFor(KEY);
	}

	public String getSubject (String jwtToken) {
		Claims claims = getClaims(jwtToken);
		return claims.getSubject();
	}

	private Claims getClaims(String jwtToken) {
		return Jwts.parser()
				.verifyWith(getSignedKey())
				.build()
				.parseSignedClaims(jwtToken)
				.getPayload();
	}

	public boolean isTokenValid (String jwtToken, String username) {
		return getSubject(jwtToken).equals(username) && !isTokenExpired(jwtToken);
	}

	private boolean isTokenExpired(String jwtToken) {
		return getClaims(jwtToken).getExpiration().before(Date.from(Instant.now()));
	}
}
