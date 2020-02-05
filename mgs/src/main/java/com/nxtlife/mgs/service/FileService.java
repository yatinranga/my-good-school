package com.nxtlife.mgs.service;

import java.util.List;


import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.FileResponse;
import com.nxtlife.mgs.view.SuccessResponse;


public interface FileService {

    public File saveMedia(FileRequest fileRequest,String category );
    
    public SuccessResponse deleteFileByCId(String cId);
	
	public SuccessResponse deleteFileByUrl(String url);
	
	public FileResponse getFileByUrl(String url);
	
	public FileResponse getFileByCId(String cId);
}
