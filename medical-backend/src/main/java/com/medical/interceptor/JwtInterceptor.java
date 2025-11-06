package com.medical.interceptor;

import com.medical.common.BusinessException;
import com.medical.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 *
 * @author medical-system
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头获取Token
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录");
        }

        String token = authorization.substring(7);

        try {
            // 解析Token获取用户类型
            String userType = jwtUtil.getUserTypeFromToken(token);

            if ("admin".equals(userType)) {
                // 管理员/医生类型
                Long adminId = jwtUtil.getAdminIdFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                Long doctorId = jwtUtil.getDoctorIdFromToken(token);

                request.setAttribute("adminId", adminId);
                request.setAttribute("role", role);

                // 如果是医生角色，设置doctorId
                if (doctorId != null) {
                    request.setAttribute("doctorId", doctorId);
                }
            } else {
                // 患者类型
                Long userId = jwtUtil.getUserIdFromToken(token);
                request.setAttribute("userId", userId);
            }

            return true;
        } catch (Exception e) {
            throw new BusinessException(401, "Token无效或已过期");
        }
    }
}
