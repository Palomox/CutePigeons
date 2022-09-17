package ga.palomox.cutepigeons.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class OryMiddleware implements HandlerInterceptor {

	/*@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		
		response.setHeader("Access-Control-Allow-Credentials", "true");
		System.out.println(response.containsHeader("Access-Control-Allow-Origin"));

	}*/
}
