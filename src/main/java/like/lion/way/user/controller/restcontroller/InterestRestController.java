package like.lion.way.user.controller.restcontroller;

import like.lion.way.user.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interest")
@RequiredArgsConstructor
public class InterestRestController {
    private final InterestService interestService;


}
