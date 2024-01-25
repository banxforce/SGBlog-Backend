package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.annotation.SystemLog;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddLinkDto;
import org.example.domain.dto.PageSelectDto;
import org.example.domain.dto.UpdateLinkDto;
import org.example.service.LinkService;
import org.example.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/content/link")
@Tag(name = "友链相关")
@PreAuthorize("@permissionService.hasPermission('content:link:list')")
public class LinkController {

    private final LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PreAuthorize("@permissionService.hasPermission('content:link:query')")
    @Operation(description = "分页查询友链列表")
    @SystemLog(businessName = "分页查询友链列表")
    @GetMapping("/list")
    public ResponseResult<Object> pageList(PageSelectDto pageSelectDto){
        return linkService.pageList(pageSelectDto);
    }

    @PreAuthorize("@permissionService.hasPermission('content:link:add')")
    @Operation(description = "新增友链")
    @SystemLog(businessName = "新增友链")
    @PostMapping
    public ResponseResult<Object> addLink(@RequestBody AddLinkDto addLinkDto){
        // 参数是否非法
        boolean illegal = DtoUtils.hasIllegalParameter(addLinkDto, AddLinkDto.class) ||
                !List.of(SystemConstants.LINK_STATUS_PASS, SystemConstants.LINK_STATUS_NOT_PASS, SystemConstants.LINK_STATUS_NOT_PROCESS)
                        .contains(addLinkDto.getStatus());
        if(illegal) return null;
        return linkService.addLink(addLinkDto);
    }

    @PreAuthorize("@permissionService.hasPermission('content:link:edit')")
    @Operation(description = "修改友链-根据id查询友链")
    @SystemLog(businessName = "修改友链-根据id查询友链")
    @GetMapping("/{id}")
    public ResponseResult<Object> getLinkById(@PathVariable Long id){
        return linkService.getLinkById(id);
    }

    @PreAuthorize("@permissionService.hasPermission('content:link:edit')")
    @Operation(description = "修改友链-更新友链")
    @SystemLog(businessName = "修改友链-更新友链")
    @PutMapping
    public ResponseResult<Object> updateLink(@RequestBody UpdateLinkDto updateLinkDto){
        // 参数是否非法
        boolean illegal = DtoUtils.hasIllegalParameter(updateLinkDto, UpdateLinkDto.class) ||
                !List.of(SystemConstants.LINK_STATUS_PASS, SystemConstants.LINK_STATUS_NOT_PASS, SystemConstants.LINK_STATUS_NOT_PROCESS)
                        .contains(updateLinkDto.getStatus());
        if(illegal) return null;
        return linkService.updateLink(updateLinkDto);
    }

    @PreAuthorize("@permissionService.hasPermission('content:link:remove')")
    @Operation(description = "删除友链")
    @SystemLog(businessName = "删除友链")
    @DeleteMapping("/{id}")
    public ResponseResult<Object> deleteById(@PathVariable Long... id){
        return linkService.deleteByIds(new ArrayList<>(List.of(id)));
    }

}
