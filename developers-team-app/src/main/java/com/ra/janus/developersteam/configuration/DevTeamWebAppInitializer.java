package com.ra.janus.developersteam.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DevTeamWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {WebConfig.class};
    }

    protected Class<?>[] getServletConfigClasses()  {
        return new Class<?>[] {};
    }

    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
