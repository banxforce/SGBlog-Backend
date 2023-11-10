package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddTagDto;
import org.example.domain.dto.PutTagDto;
import org.example.domain.dto.TagListDto;
import org.example.service.TagService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "标签")
@RequestMapping("/content/tag")
@PreAuthorize("@permissionService.hasPermission('content:tag:index')")
public class TagController {

    @Resource
    private TagService tagService;

    @Operation(description = "分页查询标签列表,可以根据标签名或备注")
    @SystemLog(businessName = "分页查询标签列表,可以根据标签名或备注")
    @GetMapping("/list")
    public ResponseResult<Object> list(long pageNum, long pageSize, TagListDto tagListDto){
        return tagService.pageListTag(pageNum,pageSize,tagListDto);
    }

    @PreAuthorize("@permissionService.hasPermission('content:tag:index')")
    @Operation(description = "新增标签")
    @SystemLog(businessName = "新增标签")
    @PostMapping
    public ResponseResult<Object> addTag(@RequestBody AddTagDto addTagDto){
        return tagService.addTag(addTagDto);
    }

    @PreAuthorize("@permissionService.hasPermission('content:tag:index')")
    @Operation (description = "删除标签")
    @SystemLog(businessName = "删除标签")
    @DeleteMapping("/{id}")
    public ResponseResult<Object> deleteTag(@PathVariable Long id){
        return tagService.deleteTagById(id);
    }

    @PreAuthorize("@permissionService.hasPermission('content:tag:index')")
    @Operation(description = "按id查询标签")
    @SystemLog(businessName = "按id查询标签")
    @GetMapping("/{id}")
    public ResponseResult<Object> getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }

    @PreAuthorize("@permissionService.hasPermission('content:tag:index')")
    @Operation(description = "修改标签")
    @SystemLog(businessName = "修改标签")
    @PutMapping
    public ResponseResult<Object> updateTag(@RequestBody PutTagDto putTagDto){
        return tagService.updateTag(putTagDto);
    }

    @Operation(description = "查询所有标签")
    @SystemLog(businessName = "查询所有标签")
    @GetMapping("/listAllTag")
    public ResponseResult<Object> listAllTag(){
        return tagService.listAllTag();
    }

}
