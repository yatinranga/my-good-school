package com.nxtlife.mgs.store;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.ex.UploadException;


@Service
public class FileSystemFileStore implements FileStore {

	private static Logger logger = LoggerFactory.getLogger(FileSystemFileStore.class);

	  @Value("${mgs.file.upload.location}")
	  private String uploaddir;

	  private Path uploadpath;

	  @PostConstruct
	  public void init()
	  {
	    uploadpath = Paths.get(uploaddir);
	    if (uploadpath.toFile().exists())
	    {
	      if (!uploadpath.toFile().isDirectory())
	      {
	        logger.error("{} is not a dir, cant continue", uploaddir);
	        throw new UploadException(uploaddir + "is not a dir, cant continue");
	      }
	    }
	    else
	    {
	      if (!uploadpath.toFile().mkdirs())
	      {
	        logger.error("cannot create upload directory", uploaddir);
	        throw new UploadException(uploaddir + "cannot create upload directory!");
	      }
	    }
	  }

	  @Override
	  public String store(String category, String filename, byte[] data) throws IOException
	  {
	    logger.info("local fileSystem called..{}");
	    Path finalLocation = uploadpath.resolve(category);
	    if (!finalLocation.toFile().exists())
	    {
	      if (!finalLocation.toFile().mkdirs())
	      {
	        logger.error("cannot create upload directory", finalLocation);
	        throw new UploadException(finalLocation + "cannot create upload directory!");
	      }
	    }
	    String filepath = finalLocation.resolve(filename).toString();
	    BufferedOutputStream stream =
	        new BufferedOutputStream(new FileOutputStream(new File(filepath)));
	    stream.write(data);
	    stream.close();
	    return filepath;
	  }
	
	  @Override
	  public String store(String category, File file)
	  {
	    Path finalLocation = uploadpath.resolve(category);
	   
	    if (!finalLocation.toFile().exists())
	    {
	      if (!finalLocation.toFile().mkdirs())
	      {
	        logger.error("cannot create upload directory", finalLocation);
	        throw new UploadException(finalLocation + "cannot create upload directory!");
	      }
	    }

	    try
	    {
	      Files.copy(Paths.get(file.getAbsolutePath()), finalLocation.resolve(file.getName()));
	      return finalLocation.toString();
	    }
	    catch (IOException e)
	    {
	      logger.error("Error copying file!", e);
	      throw new UploadException(e);
	    }
	  }


}

