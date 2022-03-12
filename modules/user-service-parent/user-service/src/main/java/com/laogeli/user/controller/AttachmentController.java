package com.laogeli.user.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.net.HttpHeaders;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.FileUtil;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.Servlets;
import com.laogeli.common.core.vo.AttachmentVo;
import com.laogeli.common.core.web.BaseController;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.user.api.module.Attachment;
import com.laogeli.user.service.AttachmentService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 附件信息管理
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Slf4j
@AllArgsConstructor
@Api("附件信息管理")
@RestController
@RequestMapping("/v1/attachment")
public class AttachmentController extends BaseController {

    private final AttachmentService attachmentService;

    /**
     * 根据ID获取
     *
     * @param id id
     * @return ResponseBean
     */
    @ApiOperation(value = "获取附件信息", notes = "根据附件id获取附件详细信息")
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/{id}")
    public ResponseBean<Attachment> attachment(@PathVariable String id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return new ResponseBean<>(attachmentService.get(attachment));
    }

    /**
     * 分页查询
     *
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param sort       sort
     * @param order      order
     * @param attachment attachment
     * @return PageInfo
     */
    @GetMapping("attachmentList")
    @ApiOperation(value = "获取附件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "attachment", value = "附件信息", dataType = "Attachment")
    })
    public PageInfo<Attachment> attachmentList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                               Attachment attachment) {
        return attachmentService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), attachment);
    }

    /**
     * 上传文件
     *
     * @param file       file
     * @param attachment attachment
     */
    @PostMapping("upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busiType", value = "业务分类", dataType = "String"),
            @ApiImplicitParam(name = "busiId", value = "业务Id", dataType = "String"),
            @ApiImplicitParam(name = "busiModule", value = "业务模块", dataType = "String"),
    })
    @Log(value = "上传文件", type = 0)
    public ResponseBean<Attachment> upload(@ApiParam(value = "要上传的文件", required = true) @RequestParam("file") MultipartFile file,
                                           Attachment attachment) {
        System.out.println(file);
        System.out.println(attachment);
        try {
            if (file.isEmpty())
                return new ResponseBean<>(new Attachment());
            ByteArrayInputStream inputStream;
            if (FileUtil.getFileNameEx(file.getOriginalFilename()).equals("jpg") || FileUtil.getFileNameEx(file.getOriginalFilename()).equals("png")) {
                ByteArrayOutputStream outputStreamut = new ByteArrayOutputStream();
                Thumbnails.of(file.getInputStream())
                        .scale(1f) // 值在0到1之间,1f就是原图大小,0.5就是原图的一半大小
                        .outputQuality(0.5f) // 值也是在0到1,越接近于1质量越好,越接近于0质量越差
                        .toOutputStream(outputStreamut);
                inputStream = new ByteArrayInputStream(outputStreamut.toByteArray());
                MultipartFile newFile = new MockMultipartFile(file.getOriginalFilename(), file.getOriginalFilename(), ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                return new ResponseBean<>(attachmentService.upload(newFile, attachment));
            } else {
                MultipartFile newFile = new MockMultipartFile(file.getOriginalFilename(), file.getOriginalFilename(), ContentType.APPLICATION_OCTET_STREAM.toString(), file.getInputStream());
                return new ResponseBean<>(attachmentService.upload(newFile, attachment));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("图片上传压缩失败");
        }
    }




    /**
     * 下载文件
     *
     * @param id id
     */
    @GetMapping("download")
    @ApiOperation(value = "下载附件", notes = "根据ID下载附件")
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, dataType = "String")
    public void download(@NotBlank String id, HttpServletRequest request, HttpServletResponse response) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            attachment = attachmentService.get(attachment);
            if (attachment == null)
                throw new CommonException("附件不存在！");
            inputStream = attachmentService.download(attachment);
            // 输出流
            outputStream = response.getOutputStream();
            response.setContentType("application/zip");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=10");
            // IE之外的浏览器使用编码输出名称
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, attachment.getAttachName()));
            response.setContentLength(inputStream.available());
            // 下载文件
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 删除附件
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除附件", notes = "根据ID删除附件")
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, paramType = "path")
    @Log(value = "删除附件", type = 0)
    public ResponseBean<Boolean> delete(@PathVariable String id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        attachment = attachmentService.get(attachment);
        boolean success = false;
        if (attachment != null)
            success = attachmentService.delete(attachment) > 0;
        return new ResponseBean<>(success);
    }

    /**
     * 批量删除
     *
     * @param attachment attachment
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除附件", notes = "根据附件id批量删除附件")
    @ApiImplicitParam(name = "attachment", value = "附件信息", dataType = "Attachment")
    @Log(value = "批量删除附件", type = 0)
    public ResponseBean<Boolean> deleteAllAttachments(@RequestBody Attachment attachment) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(attachment.getIdString()))
                success = attachmentService.deleteAll(attachment.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除附件失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据附件ID批量查询
     *
     * @param attachmentVo attachmentVo
     * @return ResponseBean
     */
    @PostMapping(value = "findById")
    @ApiOperation(value = "批量查询附件信息", notes = "根据附件ID批量查询附件信息")
    @ApiImplicitParam(name = "attachmentVo", value = "附件信息", dataType = "AttachmentVo")
    public ResponseBean<List<AttachmentVo>> findById(@RequestBody AttachmentVo attachmentVo) {
        ResponseBean<List<AttachmentVo>> returnT = null;
        Attachment attachment = new Attachment();
        attachment.setIds(attachmentVo.getIds());
        List<Attachment> attachmentList = attachmentService.findListById(attachment);
        if (CollectionUtils.isNotEmpty(attachmentList)) {
            // 流处理转换成AttachmentVo
            List<AttachmentVo> attachmentVoList = attachmentList.stream().map(tempAttachment -> {
                AttachmentVo tempAttachmentVo = new AttachmentVo();
                BeanUtils.copyProperties(tempAttachment, tempAttachmentVo);
                return tempAttachmentVo;
            }).collect(Collectors.toList());
            returnT = new ResponseBean<>(attachmentVoList);
        }
        return returnT;
    }

    /**
     * 获取预览地址
     *
     * @param id id
     * @return ResponseBean
     */
    @GetMapping("/{id}/preview")
    @ApiOperation(value = "获取预览地址", notes = "根据附件ID获取预览地址")
    @ApiImplicitParam(name = "id", value = "附件id", required = true, dataType = "String", paramType = "path")
    public ResponseBean<String> getPreviewUrl(@PathVariable String id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return new ResponseBean<>(attachmentService.getPreviewUrl(attachment));
    }

    private static String ZIP_FILE="E:/pic/test.zip";
    private static String JPG_FILE_PATH="E:/pic/";

    @GetMapping("downloadZIP")
    public void batchDown(@RequestParam String filePath, HttpServletResponse response) {
        /*System.out.println("程序运行");
        long startTime = System.currentTimeMillis(); //获取开始时间
        //判断参数是否有效
        if (null == filePath || StringUtils.isEmpty(filePath) || filePath.split(",").length == 0) return;
        String[] urls = filePath.split(",");
        try {
            //设置下载的文件名称, 注意中文名需要做编码类型转换
            String filename = "test.zip";
            response.setContentType("application/x-octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
            response.setCharacterEncoding("utf-8");
            // 创建zip输出流
            // ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

            byte[] buffer = new byte[1024 * 64];
            // 创建一个新的 byte 数组输出流，它具有指定大小的缓冲区容量
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 创建一个新的缓冲输出流，以将数据写入指定的底层输出流
            BufferedOutputStream fos = new BufferedOutputStream(baos);
            ZipOutputStream zos = new ZipOutputStream(fos);
            int index = 1;
            for (String url : urls) {

                //根据文件地址获取输入流
                InputStream is = new URL(url).openConnection().getInputStream();
                //设置zip里面每个文件的名称
                zos.putNextEntry(new ZipEntry("文件名" + index + ".jpg"));
                int length;
                while ((length = is.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                is.close();
                index++;
            }
            zos.close();
            zos.flush();
            try (OutputStream _os = response.getOutputStream(); InputStream is = new ByteArrayInputStream(baos.toByteArray());) {
                byte[] buffer1 = new byte[1024 * 5];
                int len = 0;
                while ((len = is.read(buffer1)) > 0) {
                    _os.write(buffer1, 0, len);
                }
                _os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        long begin = System.currentTimeMillis();

        String filename = "test.zip";
        response.setContentType("application/x-octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
        response.setCharacterEncoding("utf-8");

        File file = new File(ZIP_FILE);
        try (ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
             WritableByteChannel channel = Channels.newChannel(zip)) {
            for (int i = 0 ; i < 10 ; i ++) {
                System.out.println("compress at " + i + " time");
                try(FileChannel fileChannel = new FileInputStream(JPG_FILE_PATH + i + ".jpg").getChannel()){
                    zip.putNextEntry(new ZipEntry(i + ".jpg"));
                    fileChannel.transferTo(0, 3 * 1000 * 1000, channel);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("consumer time is " + (System.currentTimeMillis() - begin));
    }

}
