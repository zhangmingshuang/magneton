package org.magneton.spy;

import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import org.magneton.spy.protocol.DuplicateProtoclException;
import org.magneton.spy.protocol.Protocol;

/**
 * @author zhangmsh 2021/7/28
 * @since 1.0.0
 */
public class Spy {
  private static final Map<String, Protocol> protocols = Maps.newConcurrentMap();

  static {
    ServiceLoader<Protocol> loader = ServiceLoader.load(Protocol.class);
    loader.forEach(
        protocol -> {
          String name = protocol.name().toUpperCase(Locale.ROOT);
          Protocol exist = protocols.put(name, protocol);
          if (exist != null) {
            throw new DuplicateProtoclException(name, protocol.getClass(), exist.getClass());
          }
        });
  }

  static Protocol protocol(String protocol) {
    return protocols.get(protocol.toUpperCase(Locale.ROOT));
  }
}
