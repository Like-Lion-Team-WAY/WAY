package like.lion.way.user.controller.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import like.lion.way.chat.service.ChatService;
import like.lion.way.file.service.S3Service;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {


    private final UserService userService;

    private final S3Service s3Service;

    private final ChatService chatService;

    /**
     * username 중복체크
     * @param username
     */
    @GetMapping("/duplicate")
    public ResponseEntity<Boolean> duplicateCheck(@RequestParam("username") String username){
        User user =userService.findByUsername(username);
        if(user==null){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }

    /**
     *  관심 등록
     * @param interests 내가 선택한 관심 이름들
     */
    @PostMapping("/addInterests")
    public ResponseEntity<String> addInterests(HttpServletRequest request
                                                , HttpServletResponse response
                                                , @RequestBody Set<String> interests){
        User user = userService.addInterests(request,response,interests);
        if(user.getInterests()!=null){
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.ok("fail");
    }

    /**
     *  이미지 수정 or 저장
     * @param file
     * @param deleteFileName
     */
    @PostMapping("/updateOrSaveImg")
    public ResponseEntity<String> updateOrSaveImg(@RequestParam("image") MultipartFile file
                                                ,@RequestParam("existingImageName") String deleteFileName
                                                , HttpServletRequest request){
        if(deleteFileName!=null){
            s3Service.deleteFile(deleteFileName);
        }
        String key = s3Service.uploadFile(file);
        return  userService.updateOrSaveImg(deleteFileName, request , key);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(HttpServletRequest request , HttpServletResponse response){
        User user = userService.getUserByToken(request);
        chatService.withdrawProcessing(user);
        userService.deleteUser(user.getUserId());
        User delUser = userService.findByUserId(user.getUserId());
        if(delUser==null){
            userService.deleteCookie(response);
            return ResponseEntity.ok("success");

        }else{
            return ResponseEntity.ok("fail");
        }
    }

}
