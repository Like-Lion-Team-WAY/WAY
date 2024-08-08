let idCheck=true;

$('#user-id').change(function(){
    idCheck=false;
    let id = $('#user-id').val();
    $('#profile-url').val('http://localhost:8080/post/'+id);
})
$('#duplicateCheck').click(function(e){
    e.preventDefault();
    let username = $('#user-id').val();
    $.ajax({
        url: '/user/duplicate',
        method: 'GET',
        dataType: 'json',
        data:{
            username: username
        },
        success:function(result){
            if (result) {
                // 사용 가능한 아이디 (true)
                $("#stateMessage").text("사용 가능한 아이디입니다.").css("color", "green").show();
                idCheck=true;
            } else {
                // 중복된 아이디 (false)
                $("#stateMessage").text("이미 해당 아이디가 존재합니다.").css("color", "red").show();
                idCheck=false;
            }
        },
        error:function(err){
            console.log(err);
        }
    })
})
$('.btn-save').click(function(e){
    e.preventDefault();
    if(idCheck){
        $('#updateForm').submit();
    }
})
$('#file-input').change(function() {
    let imgSrc = $('.user-icon').attr('src');

// 삭제할 파일이름
    let urlParams = new URLSearchParams(imgSrc.split('?')[1]);
    let imgName = urlParams.get('filename');

   //등록할 사진
    let fileInput = $('#file-input')[0];
    let file = fileInput.files[0];

    let formData = new FormData();
    formData.append("image",file);
    formData.append('existingImageName' , imgName);
    $.ajax({
        url:'/user/updateOrSaveImg',
        method:'POST',
        data: formData,
        contentType: false,
        processData: false,
        success:function(result){
            if (result !== 'fail') {
                // `result`가 새로운 파일 이름이라고 가정하고, 새로운 이미지 URL로 `src` 업데이트
                $('.user-icon').attr('src', '/display?filename=' + result);
            } else {
                console.error('Image update failed.');
            }
        },
        error:function(err){
            console.log(err);
        }
    })
})

$(document).ready(function () {
    viewAll();
    $('#viewAll').click(function(){
        viewAll();
    });
    let availableContainer = $(".available-tags");
    let selectedContainer = $(".selected-tags");
    //전체 태그 불러오기
    function viewAll(){
        $.ajax({
            url:'/interest/all',
            method:'GET',
            success:function(result){
                availableContainer.empty();
                $.each(result, function(index, tag) {
                    let tagElement = $("<div>").text(tag.name).appendTo(availableContainer);


                    tagElement.on("click", function () {
                        if (!isTagAlreadySelected(tag.name)) {
                            $(this).appendTo(selectedContainer);
                        }
                    });
                });
            },
            error:function (err){
                console.log(err);
            }
        })
    }
    //이미선택했는지 체크
    function isTagAlreadySelected(tagName) {
        let isSelected = false;
        selectedContainer.find("div").each(function() {
            if ($(this).text() === tagName) {
                isSelected = true;
            }
        });
        return isSelected;
    }

    //검색에 따라 밑 요소 바뀌게
    $('#tagInput').on('input',function(){
        let keyword = $('#tagInput').val();
        $.ajax({
            url:'/interest/search?keyword='+keyword,
            method: 'GET',
            success:function (result){
                console.log(result);
                availableContainer.empty();
                $.each(result, function(index, tag) {
                    console.log(tag);
                    let tagElement = $("<div>").text(tag).appendTo(availableContainer);


                    tagElement.on("click", function () {
                        if (!isTagAlreadySelected(tag.name)) {
                            $(this).appendTo(selectedContainer);
                        }
                    });
                });
            },error:function(err){
                console.log(err);
            }
        })
    })

    //완료버튼 클릭
    $('#finishBtn').click(function(){
        selectedContainer = $(".selected-tags");
        let value = [];
        selectedContainer.find("div").each(function() {
            value.push($(this).text());
        });
        $.ajax({
            url:'/user/addInterests',
            method:'POST',
            contentType:'application/json',
            data: JSON.stringify(value),
            success:function(result){
                if(result!=='success'){
                    alert('관심 설정 오류');
                }
                location.reload()

            },
            error:function(err){
                console.log(err);
            }
        })
    })

    //회원탈퇴버튼 클릭
    $('.btn-cancel').click(function(){
        Swal.fire({
            title: '정말로 탈퇴 하시겠습니까?',
            text: '탈퇴를 진행하면 회원정보가 전부 삭제됩니다',
            icon: 'warning',

            showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
            confirmButtonColor: '#d33', // confrim 버튼 색깔 지정
            cancelButtonColor: '#3085d6', // cancel 버튼 색깔 지정
            confirmButtonText: '탈퇴', // confirm 버튼 텍스트 지정
            cancelButtonText: '취소', // cancel 버튼 텍스트 지정

            reverseButtons: false, // 버튼 순서 거꾸로

        }).then(result => {
            // 만약 Promise리턴을 받으면,
            if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
                $.ajax({
                    url:'/user/deleteUser',
                    method:'DELETE',
                    success:function(result){
                        if(result==='success'){
                            window.location.href="/user/login"
                        }else{
                            Swal.fire('회원탈퇴에 실패하였습니다.', 'error');
                        }


                    },
                    error:function(err){
                        console.log(err);
                    }
                })

            }
        });

    })
});