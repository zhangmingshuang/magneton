package org.magneton.enhance.wechat.mp.core.menu;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.magneton.core.Result;

/**
 * 菜单管理.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Slf4j
public class MpMenuManagement implements MenuManagement {

	private final WxMpService wxService;

	public MpMenuManagement(WxMpService wxService) {
		this.wxService = wxService;
	}

	@Override
	public Result<Void> create(String json) {
		WxMenu wxMenu = WxMenu.fromJson(json);
		try {
			this.wxService.getMenuService().menuCreate(wxMenu);
			return Result.success();
		}
		catch (WxErrorException e) {
			log.error(String.format("创建菜单[%s]失败", json), e);
			return Result.failBy(e.getError().getErrorMsg());
		}
	}

}