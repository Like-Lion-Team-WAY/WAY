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
                window.location.href='/main'

            },
            error:function(err){
                console.log(err);
            }
        })
    })
});