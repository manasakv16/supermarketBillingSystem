package com.example.SalesApp.supermarketBillingSystem.security.service;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    String privateKeyFilePath = "";
    String publicKeyFilePath = "";
    KeyPair keyPair = generateRsaKeyPair();
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();
    public JwtServiceImpl() throws NoSuchAlgorithmException {
    }
    public static void writeKeysIntoFile(KeyPair keyPair) {
        String publicKeyFilePath = "\\D:\\Java Projects\\supermarketBillingSystem\\src\\main\\resources\\templates\\public_key.pem";
		String privateKeyFilePath = "\\D:\\Java Projects\\supermarketBillingSystem\\src\\main\\resources\\templates\\private_key.pem";
		try (FileOutputStream fos = new FileOutputStream(publicKeyFilePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
			byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
			dos.write(publicKeyBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (FileOutputStream fos = new FileOutputStream(privateKeyFilePath);
			 DataOutputStream dos = new DataOutputStream(fos)) {
			byte[] publicKeyBytes = keyPair.getPrivate().getEncoded();
			dos.writeInt(publicKeyBytes.length);
			dos.write(publicKeyBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public static PrivateKey readPrivateKeyFromFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             DataInputStream dis = new DataInputStream(fis)) {
            int length = dis.readInt();
            byte[] privateKeyBytes = new byte[length];
            dis.readFully(privateKeyBytes);

            // Convert the byte array to a PublicKey object
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(publicKeySpec);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Autowired
    private UserRepository userRepository;

    private static KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair1 = keyPairGenerator.generateKeyPair();
        System.out.println("private key: " + keyPair1.getPrivate());
        System.out.println("public key: " + keyPair1.getPublic());
        return keyPair1;
    }

    @Override
    public String generateToken(HashMap<String, Object> claims, UserDetails userDetails) throws Exception {
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private <T>T extractClaim(String token, Function<Claims, T> claimsResolver) throws IOException {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) throws IOException {
        return Jwts.parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] key = Decoders.BASE64.decode("413F4428472B4B6250655368566D5970337336763979244226452948404D6351");
        return Keys.hmacShaKeyFor(key);
    }

    public String extractUserName(String token) throws IOException {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) throws IOException {
        final String username = extractUserName(token);
        return ((username.equals(userDetails.getUsername())) && !isTokenExpired(token) && checkTokenId(token));
    }

    public String generateRefreshToken(HashMap<String, Object> extraClaims, UserDetails userDetails) throws Exception {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean verifyJwtToken(String jwtToken, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(publicKey)
                                .build()
                                .parseClaimsJws(jwtToken)
                                .getBody();

            System.out.println("JWT Token Subject: " + claims.getSubject());
            System.out.println("JWT Token Issuer: " + claims.getIssuer());
            System.out.println("JWT Token Expiration: " + claims.getExpiration());

            // Validate user name
            String user = claims.getSubject();
            if (user == null || user.isEmpty() || user.equals(userDetails.getUsername())) {
                System.out.println("Invalid user ID");
                return false;
            }

            // Validate expiration
            if (claims.getExpiration().before(new Date())) {
                System.out.println("Token has expired");
                return false;
            }
            // You can add additional validation checks based on your requirements
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isTokenExpired(String token) throws IOException {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean checkTokenId(String token) throws IOException {
        final String username = extractUserName(token);
        final User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user or password"));

        final Claims claims = extractAllClaim(token);
        final Object key = claims.get("key");
        final String userKey = user.getKey();
        if(userKey == null)
            return false;
        return key.toString().equals(user.getKey());
    }
}
