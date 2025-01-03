function renderPagination(responseDTO) {
    const container = document.getElementById("pagination-container");
    if (!responseDTO || responseDTO.total === 0) {
        container.innerHTML = ''; // 공란으로 설정
        return;
    }

    const { page, size, total, start, end, prev, next } = responseDTO;

    const totalPages = Math.ceil(total / size); // 전체 페이지 수 계산

    let pageHTML = `
        <div class="pagination">
            ${prev ? `<a class="page-link" data-page="${page - 1}">&laquo; </a>` : ''}
    `;

    for (let i = start; i <= end; i++) {
        pageHTML += `
            <a class="page-link ${i === page ? 'active' : ''}" data-page="${i}">
                ${i}
            </a>
        `;
    }

    pageHTML += `
            ${next ? `<a class="page-link" data-page="${page + 1}"> &raquo;</a>` : ''}
        </div>
    `;

    container.innerHTML = pageHTML;

    // 페이지 링크 클릭 이벤트 바인딩 (이벤트 위임 사용)
    container.addEventListener("click", function(event) {
        if (event.target.classList.contains("page-link")) {
            const selectedPage = parseInt(event.target.getAttribute("data-page"));
            console.log("Page selected:", selectedPage);

            // 모든 링크에서 active 클래스 제거
            const pageLinks = container.querySelectorAll(".page-link");
            pageLinks.forEach(link => link.classList.remove("active"));

            // 현재 클릭된 링크에 active 클래스 추가
            event.target.classList.add("active");

            alert("??");

            // 페이지 데이터 새로고침
            fetchNewPageData(selectedPage);
        }
    });
}

function fetchNewPageData(selectedPage) {
    alert("!!");
    // 현재 URL에서 파라미터 가져오기
    const currentParams = new URLSearchParams(window.location.search);

    // 'page' 파라미터 갱신 (이미 있으면 갱신, 없으면 추가)
    currentParams.set('page', selectedPage);

    // 새로 갱신된 쿼리 문자열 생성
    const newQueryString = currentParams.toString();

    // 현재 URL의 pathname에 새로운 쿼리 문자열을 붙여서 새로운 URL 생성
    const newUrl = `${window.location.pathname}?${newQueryString}`;

    // 새로운 URL로 페이지의 상태를 갱신
    window.history.pushState(null, '', newUrl);

    // 페이지 새로 고침
    window.location.reload();
}
