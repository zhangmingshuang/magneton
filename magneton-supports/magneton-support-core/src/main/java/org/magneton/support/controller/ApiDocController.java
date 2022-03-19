package org.magneton.support.controller;

import org.magneton.support.doc.ApiHtmlDoc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1231.
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 * @ignore ignore the api generator.
 * @see org.magneton.support.constant.RequestMappings#SUPPORT_IGNORE_MAPPERS
 */
@RestController
@RequestMapping("/api-doc.html")
public class ApiDocController {

	@RequestMapping("")
	public String body() {
		return ApiHtmlDoc.getBody();
	}

}
