package com.bee.redisflag.core;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author weiwei 全局拦截器
 *
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		String requestURI = request.getRequestURI();
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(requestURI).build();
		List<String> pathSegments = uriComponents.getPathSegments();

		if (modelAndView != null) {
			Map<String, Object> model = modelAndView.getModel();
			model.put("Path_Segments", pathSegments);
			model.put("NAV_TOP_NODES", NavHelper.topNavNodes);
			model.put("Nav_Current_Node", NavHelper.getNavNodeByPath(requestURI));
			model.put("Nav_Node_Link", NavHelper.getNavNodeLinkByPath(requestURI));
		}
	}

}
