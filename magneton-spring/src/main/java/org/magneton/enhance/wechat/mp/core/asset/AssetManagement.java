package org.magneton.enhance.wechat.mp.core.asset;

import org.magneton.core.Result;
import org.magneton.enhance.wechat.mp.core.asset.pojo.MpMaterialVoiceUploadReq;

/**
 * 素材管理
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface AssetManagement {

	/**
	 * 上传语音素材
	 * @param req 上传请求
	 * @return 上传结果, 返回素材的media_id
	 */
	Result<String> uploadVoice(MpMaterialVoiceUploadReq req);

	/**
	 * 删除素材
	 * @param mediaId 素材id
	 * @return 删除结果,如果删除成功，返回删除的对应的mediaId
	 */
	Result<String> deleteMedia(String mediaId);

}