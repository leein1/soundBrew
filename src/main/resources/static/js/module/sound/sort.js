// ** 다운로드순, 이름순, 길이순에 사용되는 정렬 **

// 정렬 버튼 클릭 시 드롭다운 메뉴 표시/숨기기
// #sortIcon과 #sortKeyword 둘 다 클릭 시 동일한 동작 적용
// #sortIcon : "정렬" 옆 이모티콘 의미 / sortKeyword : "정렬" 단어 부분 의미
document.querySelectorAll('#sortIcon, #sortKeyword').forEach(function(element) {
    // #sortIcon과 #sortKeyword 클릭 이벤트 리스너 추가
    element.addEventListener('click', function() {
        var dropdown = document.getElementById('musicSortMenu');
        
        // 드롭다운 메뉴의 현재 상태에 따라 보이거나 숨기기
        // 드롭다운이 보이지 않으면 보이게, 보이면 숨기게 변경
        if (dropdown.style.display === "none" || dropdown.style.display === "") {
            dropdown.style.display = "block";  // 드롭다운 보이기
        } else {
            dropdown.style.display = "none";   // 드롭다운 숨기기
        }
    });
});
     
// 드롭다운의 옵션 선택 시 정렬 작업을 수행할 수 있도록 설정
document.querySelectorAll('#musicSortMenu li').forEach(function(item) {
    // 드롭다운 메뉴 내 각 리스트 아이템에 클릭 이벤트 리스너 추가
    item.addEventListener('click', function() {
        // data-sort 속성 값으로 어떤 정렬을 선택했는지 확인
        var sortType = this.getAttribute('data-sort');
        console.log(sortType + " 정렬 선택됨"); // 선택된 정렬 타입을 콘솔에 출력

        // 선택된 정렬 타입에 맞는 작업을 여기에 추가할 수 있음
        // 예를 들어 리스트를 정렬하는 로직을 작성 가능

        // 정렬 옵션 선택 후 드롭다운 메뉴 닫기
        document.getElementById('musicSortMenu').style.display = 'none';
    });
});

// 외부 클릭 시 드롭다운 메뉴 닫기
document.addEventListener('click', function(event) {
    const dropdown = document.getElementById('musicSortMenu');
    
    // 클릭된 요소가 #musicSortMenu 또는 #sortIcon, #sortKeyword 내부인지 확인
    const isClickInsideMenu = event.target.closest('#musicSortMenu, #sortIcon, #sortKeyword');
    
    // 드롭다운 영역 외부를 클릭한 경우 드롭다운 닫기
    if (!isClickInsideMenu && dropdown.style.display === 'block') {
        dropdown.style.display = 'none';
    }
}); 
