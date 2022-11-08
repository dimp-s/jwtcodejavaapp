package dev.dipesh.jwtcodejavaapp.jwt;

// import java.io.IOException;

// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.util.ObjectUtils;
// import org.springframework.web.filter.OncePerRequestFilter;

// import dev.dipesh.jwtcodejavaapp.userapi.User;

// //this filter only executes once per request
// @Component
// public class JwtTokenFilter extends OncePerRequestFilter{
//     @Autowired
//     private JwtTokenUtil jwtUtil;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//         //filter header values and check if header has authorization
//         if(!hasAuthorizationBearer(request)){
//             filterChain.doFilter(request, response);
//             return;
//         }
//         //get access token if header has authorization
//         //Authorization : Bearer <Token>
//         String accessToken = getAccessToken(request);
//         //validate access token
//         if(!jwtUtil.validateAccessToken(accessToken)){
//             filterChain.doFilter(request, response);
//             return;
//         }
//         setAuthenticationContext(accessToken, request);
//         filterChain.doFilter(request, response);

//     }


    
//     //check if header bears authorization
//     private Boolean hasAuthorizationBearer(HttpServletRequest request){
//         String header = request.getHeader("Authorization");
//         if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")){
//             return false;
//         }
//         return true;
//     }
//     //check access token in header
//     private String getAccessToken(HttpServletRequest request){
//         String header = request.getHeader("Authorization");
//         String token = header.split(" ")[1].trim();
//         return token;
//     }
//     //get auth details for current user
//     private UserDetails getUserDetails(String accessToken) {
//         User userDetails = new User();
//         String[] subjectArray = jwtUtil.getSubject(accessToken).split(",");
//         userDetails.setId(Integer.parseInt(subjectArray[0]));
//         userDetails.setEmail(subjectArray[1]);
//         return userDetails;
//     }
    
//     private void setAuthenticationContext(String accessToken, HttpServletRequest request) {
//         UserDetails userDetails = getUserDetails(accessToken);
//         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null ,null);
//         authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//     }

    
// }

 
import java.io.IOException;
 
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.dipesh.jwtcodejavaapp.userapi.User;
 
 
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtUtil;
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
 
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }
 
        String token = getAccessToken(request);
 
        if (!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }
 
        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }
 
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }
 
        return true;
    }
 
    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }
 
    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);
 
        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
 
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
 
    private UserDetails getUserDetails(String token) {
        User userDetails = new User();
        String[] jwtSubject = jwtUtil.getSubject(token).split(",");
 
        userDetails.setId(Integer.parseInt(jwtSubject[0]));
        userDetails.setEmail(jwtSubject[1]);
 
        return userDetails;
    }
}