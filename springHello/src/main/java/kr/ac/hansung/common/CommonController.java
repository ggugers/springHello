package kr.ac.hansung.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.ac.hansung.HomeController;

/**
 * Handles requests for the application home page.
 */
@Controller
public class CommonController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private static final String ROOT_UPLOAD_DIR = "/dev/upload";
	
	@RequestMapping(value = "/common/fileUploadAjax.do", method = RequestMethod.POST)
    public ModelAndView fileUploadAjax(MultipartHttpServletRequest request, ModelAndView mav ) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		String mode = request.getParameter("mode");
		String name = request.getParameter("name");
		String childFolder = "temp";
		if("stamp".equals(mode)){
			childFolder = "stamp";
		}else if("course".equals(mode)){
			childFolder = "course";
		}else if("test".equals(mode)){
			childFolder = "test";
		}else if("excel".equals(mode)){
			childFolder = "excel";
		}
		
		//folder mkdir
		File saveFolder = new File(ROOT_UPLOAD_DIR+File.separator+childFolder);
		if(!saveFolder.exists() || saveFolder.isFile()){
			saveFolder.mkdirs();
		}
		
		//iterator
		Iterator<String> files = request.getFileNames();
		MultipartFile multipartFile;
		String filePath;
		List<Map<String, Object>> fileInfoList = new ArrayList<Map<String,Object>>();
		
		try{
			
			//while
			while(files.hasNext()){			
				
				//file����
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				multipartFile = request.getFile((String)files.next());
				String fieldName = multipartFile.getName();
				String extName = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1 );
				String filename = uuid+"."+extName;
				filePath = ROOT_UPLOAD_DIR+File.separator+childFolder+File.separator+filename;
				if( filename != null && !"".equals(multipartFile.getOriginalFilename()) ){
					multipartFile.transferTo(new File(filePath));

					//file���� ���
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fieldName", fieldName);
					map.put("FilePath", filePath);
					map.put("OriginalFilename", multipartFile.getOriginalFilename());
					map.put("extName", extName);
					map.put("FileSize", multipartFile.getSize());
					map.put("FileSavedName", filename);
					fileInfoList.add(map);
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
		int sizeCheck = 0;
		if(fileInfoList != null && fileInfoList.size() > sizeCheck){
			rtnMap = fileInfoList.get(0);
		}
		mav.setView(new JSONView());
		mav.addObject("JSON_OBJECT",  fileInfoList);
		return mav;
    }
	
	
	@RequestMapping(value = "/common/fileUpload.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView fileUpload(HttpServletRequest request, ModelAndView mav ) throws Exception {

		return mav;
    }
	
	
	/** 
	 * 파일다운로드 
	 * @param request 
	 * @return 
	 * @throws Exception 
	 */ 
	 @RequestMapping(value = "/common/fileDownload.do") 
	public void downloadFileController(HttpServletRequest req, HttpServletResponse res) throws Exception { 
		 String mode = req.getParameter("mode"); 
		 String file = req.getParameter("file"); 
		 file = file.replaceAll("\\.\\.\\/", "");  // 부정접근 방지
		 logger.debug("LOGGER : 로그가 한글이 깨진다.");
		 String name = req.getParameter("name");
		 logger.debug("name = " + name);
		 name = URLEncoder.encode(name, "UTF-8").replaceAll("%27","'");  //한글깨짐 방지
		 logger.debug("name after= " + name);
		 String folder = "temp"; 
		 if("stamp".equals(mode)){ 
			 folder = "stamp"; 
		 }else if("course".equals(mode)){ 
			 folder = "course"; 
		 }else if("test".equals(mode)){ 
			 folder = "test"; 
		 }  // 폴더 접근 유효성 체크
		 
		 String fileFullPath = ROOT_UPLOAD_DIR+File.separator+folder+File.separator+file;  // 서버에 맞는 업로드 기본 폴더
		 File downFile = new File(fileFullPath);  //파일 객체 생성
		 if(downFile.isFile()){  // 파일이 존재하면
			 int fSize = (int)downFile.length(); 
			 res.setBufferSize(fSize); 
			 res.setContentType("application/octet-stream"); 
			 res.setHeader("Content-Disposition", "attachment; filename="+name+""); 
			 res.setContentLength(fSize);  // 헤더정보 입력
			 FileInputStream in  = new FileInputStream(downFile); 
			 ServletOutputStream out = res.getOutputStream(); 
			 try 
			 { 
				 byte[] buf=new byte[8192];  // 8Kbyte 로 쪼개서 보낸다.
				 int bytesread = 0, bytesBuffered = 0; 
				 while( (bytesread = in.read( buf )) > -1 ) { 
					 out.write( buf, 0, bytesread ); 
					 bytesBuffered += bytesread; 
					 if (bytesBuffered > 1024 * 1024) { //아웃풋스트림이 1MB 가 넘어가면 flush 해준다.
						 bytesBuffered = 0; 
						 out.flush(); 
					 } 
				 } 
			 } 
			 finally { 
				 if (out != null) { 
					 out.flush(); 
					 out.close(); 
				 } 
				 if (in != null) { 
					 in.close(); 
				 } 
				 //	에러가 나더라도 아웃풋 flush와 close를 실행한다.
			 } 
		 } 
	 }
	
}
