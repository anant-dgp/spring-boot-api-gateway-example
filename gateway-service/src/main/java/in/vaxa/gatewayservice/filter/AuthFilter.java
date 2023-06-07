package in.vaxa.gatewayservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

  @Autowired
  JwtTokenUtil tokenUtil;
  @Autowired
  private RouteValidator validator;

  public AuthFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {

    return ((exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      if (validator.isSecured.test(request)) {
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
          throw new RuntimeException("Missing Authorization header");
        }
        String token = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (Objects.nonNull(token) && token.startsWith("Bearer ")) {
          token = token.substring(7);
        }
        try {
          tokenUtil.validateToken(token);
        } catch (Exception e) {
          throw new RuntimeException("Invalid Token");
        }
      }

      return chain.filter(exchange);
    });
  }

  static class Config {

  }
}
