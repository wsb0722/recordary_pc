package com.fairy_pitt.recordary.endpoint.group.service;

import com.fairy_pitt.recordary.common.entity.GroupEntity;
import com.fairy_pitt.recordary.common.entity.GroupMemberEntity;
import com.fairy_pitt.recordary.common.entity.UserEntity;
import com.fairy_pitt.recordary.common.repository.GroupMemberRepository;
import com.fairy_pitt.recordary.common.repository.GroupRepository;
import com.fairy_pitt.recordary.common.repository.UserRepository;
import com.fairy_pitt.recordary.endpoint.group.dto.GroupSaveRequestDto;
import com.fairy_pitt.recordary.endpoint.group.dto.GroupResponseDto;
import com.fairy_pitt.recordary.endpoint.group.dto.GroupUpdateRequestDto;
import com.fairy_pitt.recordary.endpoint.user.dto.UserResponseDto;
import com.fairy_pitt.recordary.endpoint.user.service.UserService;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor// 검색해보기
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public Long save(GroupSaveRequestDto requestDto) {
        return groupRepository.save(requestDto.toEntity())
                .getGroupCd();
    }

    @Transactional
    public Long updateGroupInfo(Long id, GroupUpdateRequestDto groupDto) {
        GroupEntity groupEntity = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹이 없습니다. id=" + id));

        groupEntity.updateGroupInfo(groupDto.getGroupName(), groupDto.getGroupState(), groupDto.getGroupPic(), groupDto.getGroupEx());

        return id;
    }

    @Transactional
    public Long changGroupMaster(String UserId, Long groupCd) {
        GroupEntity groupEntity = groupRepository.findById(groupCd)
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹이 없습니다. id=" + groupCd));

        UserEntity User = userRepository.findByUserId(UserId);
        groupEntity.updateGroupMaster(User);

        return groupCd;
    }

    @Transactional
    public void delete(Long id) {
        GroupEntity groupEntity = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹이 없습니다. id=" + id));

        groupRepository.delete(groupEntity);
    }

    @Transactional(readOnly = true)
    public JSONArray findAllGroupInfoById(Long id) {
        JSONArray group = new JSONArray();
        JSONArray groupMemberInfoList = new JSONArray();
        GroupEntity entity = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹이 없습니다. id=" + id));

        JSONObject groupInfo = new JSONObject();
        groupInfo.put("groupCd", entity.getGroupCd());
        groupInfo.put("groupNm", entity.getGroupName());
        groupInfo.put("groupEx", entity.getGroupEx());
        groupInfo.put("groupPic", entity.getGroupPic());
        groupInfo.put("groupState", entity.getGroupState());
        groupInfo.put("groupMaster", entity.getGMstUserFK().getUserId());

        List<UserEntity> members = groupMemberRepository.findAllByGroupCodeFK(entity);
        for (UserEntity groupMember : members) {
            if (!entity.getGMstUserFK().getUserId().equals(groupMember.getUserId())) {
                JSONObject groupMemberInfo = new JSONObject();
                groupMemberInfo.put("user_id", groupMember.getUserId());
                groupMemberInfo.put("user_nm", groupMember.getUserNm());
                groupMemberInfo.put("user_ex", groupMember.getUserEx());
                groupMemberInfo.put("user_pic", null);
                groupMemberInfoList.add(groupMemberInfo);
            }
        }
        group.add(groupInfo);
        group.add(groupMemberInfoList);
        return group;
    }
}

//    @Transactional(readOnly = true)
//    public List<GroupResponseDto> findAllGroup(Long id) {
//        GroupEntity entity = groupRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
//
//        return new GroupResponseDto(entity);
//    }

//    public List<GroupMemberDto> findGroupMEmber(Long groupCd)
//    {
//        List<GroupMemberEntity> groupMemberList = groupRepository.findByGroupCd(groupCd).getMembers();
//
//    }

/*
    @Autowired
    private final GroupRepository groupRepository;
    private final UserRepository usersRepository;
    private final GroupMemberService groupMemberService;

    public GroupEntity groupCreate(GroupEntity groupEntity){
        GroupEntity resultGroupEntity = groupRepository.save(groupEntity);
        return resultGroupEntity;
    }

    public GroupEntity findGroupId(long id){
        return groupRepository.findByGroupCd(id);
    }


    //그룹 검색
    public List<GroupEntity> groupSearch(String gName){


    return groupRepository.findBygNameLike("%"+gName+"%");

    }

    public Optional<GroupEntity> findGroup(GroupEntity groupEntity){

        return groupRepository.findById(groupEntity.getGroupCd());
    }

    public Boolean groupUpdate(GroupEntity groupEntity, long id){
//        GroupEntity  thisBoardEntity = this.findGroupId(id);
//
//        //GroupEntity updateGroupEntity = thisBoardEntity.get();
//
//        thisBoardEntity.(groupEntity.getGEx());
//        thisBoardEntity.setGName(groupEntity.getGName());
//        thisBoardEntity.setGPic(groupEntity.getGPic());
//        thisBoardEntity.setGState(groupEntity.getGState());
//
//        groupRepository.save(thisBoardEntity);
         return  true;
    }

    //모든 공개그룹
    public List<GroupEntity> findAllPublicGroup()
    {
        return groupRepository.findAllBygState(true);
    }*/

