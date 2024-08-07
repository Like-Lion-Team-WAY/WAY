package like.lion.way.els.controller;


import java.util.List;
import like.lion.way.els.domain.ElsInterest;
import like.lion.way.els.service.ElsInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interest")
public class ElsInterestRestController {
    private final ElsInterestService elsInterestService;

    @GetMapping("/search")
    public List<String> searchInterests(@RequestParam("keyword") String keyword){
        return elsInterestService.searchInterestsByDescription(keyword);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ElsInterest>> getAllInterests() {
        List<ElsInterest> interests = elsInterestService.getAllInterests();
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }
    @PostMapping("/createInterest")
    public ResponseEntity<ElsInterest> createInterest(@RequestBody ElsInterest interest) {
        ElsInterest savedInterest = elsInterestService.saveInterest(interest);
        return new ResponseEntity<>(savedInterest, HttpStatus.CREATED);
    }
}
