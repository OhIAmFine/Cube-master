package cn.edu.cqupt.rubic_business.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.UserService;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet Filter implementation class SSOAuth
 */
public class SSOAuthFilter implements Filter {

    private String ssoService;

    private String cookieName;
    
    private UserService userService;

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        ssoService = fConfig.getInitParameter("SSOService");
        cookieName = fConfig.getInitParameter("cookieName");
        WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(fConfig.getServletContext());
        userService = context.getBean(UserService.class);
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getContextPath();
        String URL = ssoService + "?action=preLogin&setCookieURL=" + request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/setCookie&gotoURL=" + path;

        Cookie ticket = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    ticket = cookie;
                    break;
                }
            }
        }
        if (request.getRequestURI().equals(path + "/servlet/LogoutServlet")){
        	doLogout(request, response, ticket, URL);
        } else if (request.getRequestURI().equals(path + "/setCookie")){
        	setCookie(request, response);
        } else if (ticket != null){
//            if(request.getSession().getAttribute("email") == null){
//                authCookie(request, response,chain, ticket, URL);
//            } else {
//                chain.doFilter(request,response);
//            }
        	if(authCookie(request, response, chain, ticket, URL)){
        		chain.doFilter(request, response);
        	}

        }else{
        	notLogin(response);
            return;
        }

    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * 生成cookie，用于记录登陆信息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void setCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie ticket = new Cookie(cookieName, request.getParameter("encodedticketKey"));
        ticket.setPath(request.getContextPath()+"/");
        ticket.setMaxAge(Integer.parseInt(request.getParameter("expiry")));
        response.addCookie(ticket);
        

        String gotoURL = request.getParameter("gotoURL");
        if (gotoURL != null){
            System.out.println("SSOAuthFilter gotoURL: "+gotoURL);
        	response.sendRedirect(gotoURL);
        	return;
        }
    }

    /**
     * 登出，流程：发送到sso服务器，清除sso服务器上的记录
     *
     * @param request
     * @param response
     * @param ticket
     * @param URL
     * @throws IOException
     * @throws ServletException
     */
    private void doLogout(HttpServletRequest request, HttpServletResponse response, Cookie ticket, String URL) throws IOException, ServletException {
        NameValuePair[] params = new NameValuePair[2];
        params[0] = new NameValuePair("action", "logout");
        params[1] = new NameValuePair("cookieName", ticket.getValue());
        try {
            post(params);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
        	request.getSession().removeAttribute("user");
            response.sendRedirect(request.getContextPath());
        }
        return;
    }

    /**
     * 验证cookie
     *
     * @param request
     * @param response
     * @param ticket   本地cookie
     * @param URL      重定向的地址
     * @throws IOException
     * @throws ServletException
     */
    private boolean authCookie(HttpServletRequest request, HttpServletResponse response,FilterChain chain, Cookie ticket, String URL) throws IOException, ServletException {
        NameValuePair[] params = new NameValuePair[2];
        params[0] = new NameValuePair("action", "authTicket");
        params[1] = new NameValuePair("cookieName", ticket.getValue());
        try {
            JSONObject result = post(params);
            if (result.getBoolean("error")) {
                deleteCookie(request,response,cookieName);
                notLogin(response);
                return false;
            } else {
                //TODO 在这里保存用户信息
            	UserPo user = userService.findUserById(result.getInteger("userId"));
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
//                session.setAttribute("email", result.getString("email"));
//                session.setAttribute("userId", result.getString("userId"));
//                chain.doFilter(request,response);
                return true;
            }
        } catch (JSONException e) {
            notLogin(response);
            throw new RuntimeException(e);
        } 
    }

    /**
     * 向sso服务器发送post请求,接收json数据
     *
     * @param params   请求中携带的参数
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws JSONException
     */
    private JSONObject post(NameValuePair[] params) throws IOException, ServletException, JSONException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(ssoService);
        postMethod.addParameters(params);
        switch (httpClient.executeMethod(postMethod)) {
            case HttpStatus.SC_OK:
                return JSONObject.parseObject(postMethod.getResponseBodyAsString());
            default:
                // 其他情况
                return null;
        }
    }

    private void deleteCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
//        	System.out.println("Cookie Name: "+cookie.getName()+" - "+ cookie.getValue());
            if(cookie.getName().equals(cookieName)){
            	System.out.println("Set Cookie:"+cookie.getName()+" MaxAge = 0");
            	Cookie deleteCookie = new Cookie(cookie.getName(),null);
            	deleteCookie.setPath(request.getContextPath()+"/");
                deleteCookie.setMaxAge(0);
                response.addCookie(deleteCookie);
            }
        }
    }
    
    
    private void notLogin(HttpServletResponse response) throws IOException{
    	
    	JSONObject result = new JSONObject();
    	result.put("status", 1);
    	result.put("redirect", ssoService+"?action=preLogin");
    	response.getWriter().write(result.toJSONString());
    	
    }
    

}
