package org.magneton.enhance.wechat.mp.core.asset;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Verify;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.magneton.enhance.Result;
import org.magneton.enhance.wechat.mp.core.asset.pojo.MpMaterialVoiceUploadReq;
import org.magneton.enhance.wechat.mp.core.util.ResultUtil;

import java.io.File;

/**
 * 素材管理
 *
 * @author zhangmsh.
 * @since 2024
 */
@Slf4j
public class MaterialAssetManagement implements AssetManagement {

	private final WxMpService wxService;

	public MaterialAssetManagement(WxMpService wxService) {
		this.wxService = wxService;
	}

	@Override
	public Result<String> deleteMedia(String mediaId) {
		Preconditions.checkNotNull(mediaId, "mediaId must not be null");

		try {
			boolean res = this.wxService.getMaterialService().materialDelete(mediaId);
			if (res) {
				return Result.successWith(mediaId);
			}
			return Result.failBy("删除失败");
		}
		catch (WxErrorException e) {
			log.error("删除素材失败", e);
			return Result.valueOf(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
		}
	}

	@Override
	public Result<String> uploadVoice(MpMaterialVoiceUploadReq req) {
		Preconditions.checkNotNull(req.getFile(), "file must not be null");

		File file = req.getFile();
		Verify.verifyNotNull(file, "file must not be null");
		Verify.verify(file.exists(), "file not exists");

		String fileName = req.getFileName();
		if (Strings.isNullOrEmpty(fileName)) {
			fileName = file.getName();
		}
		else if (!fileName.contains(".")) {
			fileName = fileName + "." + file.getName().substring(file.getName().lastIndexOf(".") + 1);
		}

		String mediaType = WxConsts.MediaFileType.VIDEO;

		WxMpMaterial wxMaterial = new WxMpMaterial();
		wxMaterial.setFile(file);
		wxMaterial.setName(fileName);
		try {
			WxMpMaterialUploadResult res = this.wxService.getMaterialService().materialFileUpload(mediaType,
					wxMaterial);
			if (ResultUtil.ok(res.getErrCode())) {
				// 上传成功
				return Result.successWith(res.getMediaId());
			}
			return Result.valueOf(String.valueOf(res.getErrCode()), res.getErrMsg());
		}
		catch (WxErrorException e) {
			return Result.valueOf(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
		}
	}

}