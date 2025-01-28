function renderSearch() {
    const container = document.getElementById("searchContainer");
    container.innerHTML = ''; // Clear previous content

    const searchItem = document.createElement('div');
    searchItem.classList.add('search-sort');

    // Add HTML structure
    searchItem.innerHTML = `
        <div class="searchbar">
            <div class="searchbar-container">
                <div class="search-area">
                    <span class="searchbar-icon">
                        <img src="/images/search_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg" alt="Search Icon">
                    </span>
                    <!-- 검색 타입 드롭다운 -->
                    <select id="searchType" class="searchType">
                        <option value="t">타이틀</option>
                        <option value="n">닉네임</option>
                    </select>
                </div>
                <input type="text" id="searchKeyword" placeholder="검색..." />
            </div>
        </div><!-- end searchbar -->
    `;

    // Append the constructed searchItem
    container.appendChild(searchItem);

    function setSearchParams() {
        const urlParams = new URLSearchParams(window.location.search);
        const keyword = urlParams.get("keyword");
        const type = urlParams.get("type");

        if (keyword) {
            document.getElementById("searchKeyword").value = keyword;
        }
        if (type) {
            document.getElementById("searchType").value = type;
        }
    }

    window.onload=setSearchParams;
    window.addEventListener("popstate", setSearchParams);

    // Add event listener for the input field to handle Enter key
    document.getElementById('searchKeyword').addEventListener('keydown', handleEnterSearch);
}

/**
 * Handles search when the Enter key is pressed
 */
function handleEnterSearch(event) {
    // Check if the Enter key was pressed
    if (event.key === 'Enter') {
        // Get the keyword and type values
        const keyword = document.getElementById('searchKeyword').value.trim();
        const type = document.getElementById('searchType').value;

        // Validate input
        if (!keyword) {
            alert('검색어를 입력해주세요.');
            return;
        }

        // Get current URL parameters
        const currentParams = new URLSearchParams(window.location.search);

        // Update or add 'keyword' and 'type' parameters
        currentParams.set('keyword', keyword);
        currentParams.set('type', type);

        // Generate a new query string
        const newQueryString = currentParams.toString();

        // Construct the new URL
        const newUrl = `${window.location.pathname}?${newQueryString}`;

        // Update the browser's history state and refresh the page
        window.history.pushState(null, '', newUrl);
        window.location.reload();
    }
}
