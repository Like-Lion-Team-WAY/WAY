package like.lion.way.user.controller.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
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

    @GetMapping("/duplicate")
    public ResponseEntity<Boolean> duplicateCheck(@RequestParam("username") String username){
        User user =userService.findByUsername(username);
        if(user==null){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }

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

    @PostMapping("/updateOrSaveImg")
    public ResponseEntity<String> updateOrSaveImg(@RequestParam("image") MultipartFile file
                                                ,@RequestParam("existingImageName") String deleteFileName
                                                , HttpServletRequest request){
        String key = s3Service.uploadFile(file);

        return  userService.updateOrSaveImg(deleteFileName, request , key);
    }
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(HttpServletRequest request){
        User user = userService.getUserByToken(request);
        userService.deleteUser(user.getUserId());
        User delUser = userService.findByUserId(user.getUserId());
        if(delUser==null){
            return ResponseEntity.ok("success");
        }else{
            return ResponseEntity.ok("fail");
        }
    }

}
