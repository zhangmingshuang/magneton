package org.magneton.module.wechat.mp.core.message.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.magneton.foundation.exception.DuplicateFoundException;
import org.magneton.module.wechat.mp.core.message.mode.Mode;
import org.magneton.module.wechat.mp.core.message.mode.ModeType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理器注册表.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class MpHandlerRegistrar {

	/**
	 * 消息处理器
	 */
	private static final Map<String, MpHandler> HANDLERS = new ConcurrentHashMap<>();

	/**
	 * 注册消息处理器.
	 * @param handler 消息处理器
	 * @throws DuplicateFoundException 如果已经存在相同类型的消息处理器
	 */
	@CanIgnoreReturnValue
	public static void register(MpHandler handler) {
		Preconditions.checkNotNull(handler, "handler must not be null");

		// 支持处理的消息模式
		List<? extends Mode> supportModes = handler.supportMode();
		if (CollectionUtil.isEmpty(supportModes)) {
			return;
		}
		// 支持所有的消息模式
		for (Mode supportMode : supportModes) {
			Class<? extends MpHandler> handlerType = supportMode.handlerType();
			if (!handlerType.isInstance(handler)) {
				throw new IllegalArgumentException(
						String.format("mode %s's handler must be instance of %s", supportMode, handlerType));
			}
			Object exist = HANDLERS.put(amend(supportMode), handler);
			if (exist != null) {
				throw new DuplicateFoundException(exist, handler);
			}
		}
	}

	@Nullable
	public static <T extends Mode, H extends MpHandler> H getHandler(T mode) {
		// noinspection unchecked
		return (H) HANDLERS.get(amend(mode));
	}

	public static MpHandler unregister(MpHandler handler) {
		List<? extends Mode> supportModes = handler.supportMode();
		if (CollectionUtil.isEmpty(supportModes)) {
			return null;
		}
		for (Mode supportMode : supportModes) {
			return HANDLERS.remove(amend(supportMode));
		}
		return null;
	}

	private static String amend(Mode mode) {
		ModeType modeType = mode.modeType();
		String modeName = mode.getClass().getSimpleName();
		String suffix = "#";
		if (mode.getClass().isEnum()) {
			suffix = ((Enum<?>) mode).name();
		}
		return modeType + "-" + modeName + "-" + suffix;
	}

}