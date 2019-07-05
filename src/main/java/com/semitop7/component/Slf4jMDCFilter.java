package com.semitop7.component;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

@Component
public class Slf4jMDCFilter extends OncePerRequestFilter {
    public static final String DEFAULT_RESPONSE_CORRELATION_ID = "CID";
    public static final String DEFAULT_MDC_UUID_CID_KEY = "Slf4jMDCFilter.UUID";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain) throws java.io.IOException, ServletException {
        try {
            final String correlationId = UUID.randomUUID().toString();
            MDC.put(DEFAULT_MDC_UUID_CID_KEY, correlationId);
            response.addHeader(DEFAULT_RESPONSE_CORRELATION_ID, correlationId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(DEFAULT_MDC_UUID_CID_KEY);
        }
    }

    @Override
    protected boolean isAsyncDispatch(final HttpServletRequest request) {
        return Objects.equals(request.getDispatcherType(), DispatcherType.ASYNC);
    }
}