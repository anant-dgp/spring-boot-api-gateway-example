package in.vaxa.gatewayservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
  public static final List<String> publicEndpoints = Arrays.asList("/auth/validateToken", "/auth/authenticate");

  public Predicate<ServerHttpRequest> isSecured = request -> publicEndpoints.stream()
                                                                            .noneMatch(uri -> request.getURI()
                                                                                                     .getPath()
                                                                                                     .contains(uri));
}
