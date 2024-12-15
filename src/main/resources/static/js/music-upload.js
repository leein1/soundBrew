//
//<script>
//    // Form 데이터를 JSON으로 변환하는 함수
//    function serializeFormToNestedJSON(form) {
//        const formData = new FormData(form); // FormData 객체 생성
//        const jsonObject = {};
//
//        formData.forEach((value, key) => {
//            const keys = key.split('.'); // 중첩 키 구조 처리
//            let currentLevel = jsonObject;
//
//            keys.forEach((k, index) => {
//                if (index === keys.length - 1) {
//                    currentLevel[k] = value;
//                } else {
//                    if (!currentLevel[k]) {
//                        currentLevel[k] = {};
//                    }
//                    currentLevel = currentLevel[k];
//                }
//            });
//        });
//
//        return jsonObject;
//    }
//
//    // 곡 정보를 저장하는 배열
//    let songsArray = [];
//
//    // 곡 정보를 추가하는 함수
//    function uploadSongPush() {
//        const songTitle = document.getElementById('song-title').value;
//        const songDescription = document.getElementById('song-description').value;
//        const songTags = selectedTags;
//
//        if (!songTitle.trim()) {
//            alert("곡 제목을 입력해주세요!");
//            return;
//        }
//
//        const songInfo = {
//            title: songTitle,
//            description: songDescription,
//            tags: songTags,
//        };
//
//        songsArray.push(songInfo);
//        renderSongs();
//    }
//
//    // 곡 정보를 렌더링하는 함수
//    function renderSongs() {
//        const songInfoList = document.getElementById('song-info-list');
//        songInfoList.innerHTML = '';
//
//        if (songsArray.length === 0) {
//            songInfoList.innerHTML = '<p>추가된 곡이 없습니다.</p>';
//            return;
//        }
//
//        songsArray.forEach((song, index) => {
//            const songDiv = document.createElement('div');
//            songDiv.classList.add('song-info');
//
//            songDiv.innerHTML = `
//                <div>
//                    <h3>설명: ${song.description || '없음'}</h3>
//                    <p>제목: ${song.title}</p>
//                    <span>태그: ${song.tags.join(', ') || '없음'}</span>
//                    <button class="delete-song-btn" value="${index}">X</button>
//                </div>
//            `;
//
//            const deleteButton = songDiv.querySelector('.delete-song-btn');
//            deleteButton.addEventListener('click', () => deleteSong(index));
//
//            songInfoList.appendChild(songDiv);
//        });
//    }
//
//    // 곡 삭제 함수
//    function deleteSong(index) {
//        songsArray.splice(index, 1);
//        renderSongs();
//    }
//
//    // 최종 데이터 확인 및 서버 전송
//    async function submitData() {
//        // 앨범 정보 수집
//        const groupTitle = document.getElementById('group-name').value;
//        const groupDescription = document.getElementById('group-description').value;
//
//        if (!groupTitle.trim() || !groupDescription.trim()) {
//            alert("앨범 제목과 설명을 입력해주세요!");
//            return;
//        }
//
//        const albumData = {
//            title: groupTitle,
//            description: groupDescription,
//            songs: songsArray, // 곡 배열 포함
//        };
//
//        console.log('최종 JSON 데이터:', albumData);
//
//        try {
//            const response = await axios.post('/your-endpoint', albumData, {
//                headers: {
//                    'Content-Type': 'application/json',
//                },
//            });
//            alert("업로드 성공!");
//            console.log('Response:', response.data);
//        } catch (error) {
//            console.error('Error:', error.response ? error.response.data : error.message);
//        }
//    }
//
//    // 업로드 버튼 이벤트 핸들러
//    document.getElementById('upload-button').addEventListener('click', submitData);
//</script>

//
//
//// Store selected tags to prevent clearing when switching categories
//let selectedTags = [];
//
//// Function to show the popup for tag selection based on category
//function showTagPopup(category) {
//    let selectedTags;
//
//    switch (category) {
//        case 'instrument':
//            selectedTags = tags.instrument;  // Use tags from the 'instrument' category
//            break;
//        case 'mood':
//            selectedTags = tags.mood;  // Use tags from the 'mood' category
//            break;
//        case 'genre':
//            selectedTags = tags.genre;  // Use tags from the 'genre' category
//            break;
//        default:
//            selectedTags = [];  // Default to an empty array if no category matches
//            break;
//    }
//    // Populate popup with category-specific tags
//    const popupTagsContainer = document.getElementById('popup-tags');
//    popupTagsContainer.innerHTML = ''; // Clear previous tags
//
//    selectedTags.forEach(tag => {
//        const tagElement = document.createElement('label');
//        tagElement.innerHTML = `<input type="checkbox" value="${tag}"> ${tag}`;
//        popupTagsContainer.appendChild(tagElement);
//    });
//
//    document.getElementById('tag-popup').style.display = 'block';  // Show the popup
//}
//
//// Function to close the popup
//function closeTagPopup() {
//    document.getElementById('tag-popup').style.display = 'none';  // Hide the popup
//}
//
//// Function to search tags in the popup
//function searchTags() {
//    const searchQuery = document.getElementById('tag-search').value.toLowerCase();
//    const tags = document.querySelectorAll('#popup-tags label');
//
//    tags.forEach(tag => {
//        const tagName = tag.textContent.toLowerCase();
//        if (tagName.includes(searchQuery)) {
//            tag.style.display = 'block';  // Show matching tag
//        } else {
//            tag.style.display = 'none';  // Hide non-matching tag
//        }
//    });
//}
//
//// Function to apply selected tags from popup
//function applyTags() {
//    const checkboxes = document.querySelectorAll('#popup-tags input:checked');
//
//    checkboxes.forEach(checkbox => {
//        if (!selectedTags.includes(checkbox.value)) {
//            selectedTags.push(checkbox.value);  // Add checked tags to selectedTags array
//            const listItem = document.createElement('li');
//            listItem.textContent = checkbox.value;
//            document.getElementById('tag-list').appendChild(listItem);
//        }
//    });
//
//    closeTagPopup();  // Close the popup after applying the tags
//    enableNextButton();  // Check if we can enable the "Next" button
//}
//// Define required fields for each step
//const requiredFields = {
//    1: ['group-type'],
//    2: ['group-name', 'group-image', 'group-description'],
//    3: ['song-title','file-upload','song-description']
//};
//
//// Validation function for each step
//function validateStep(step) {
//    const fieldIds = requiredFields[step];
//    if (!fieldIds) return true; // No validation required for this step
//
//    return fieldIds.every(id => {
//        const field = document.getElementById(id);
//        return field && field.value.trim() !== '';
//    });
//}
//
//// Initialize an array to hold song information and tags
//let songsArray = [];
//
//// Modify nextStep function to display the "Add More Songs" button if album is selected
//function nextStep(step) {
//    if (!validateStep(step)) {
//        alert('모든 필수 항목을 입력해주세요.');
//    return;
//}
//
//const steps = ['type-step', 'group-info-step', 'group-song-step', 'tag-step', 'submit-step'];
//const currentStep = steps[step];
//
//// Handle display logic for "앨범에 곡 더 추가하기" button
//if (step === 4) {
//    const groupType = document.getElementById('group-type').value;
//    if (groupType === 'album') {
//        document.getElementById('add-more-songs').style.display = 'block';
//    } else {
//        document.getElementById('add-more-songs').style.display = 'none';
//    }
//}
//
//// 현재 단계 보이기
//document.getElementById(currentStep).classList.add('visible');
//
//// 이전 단계 비활성화
//if (step > 0) {
//    document.getElementById(steps[step - 1]).querySelectorAll('input, textarea, select').forEach(element => {
//        element.disabled = true; // 입력 비활성화
//    });
//}
//
//// 마지막 단계에서 업로드 버튼 활성화
//    if (step === 4) {
//        document.getElementById('upload-button').disabled = false;
//    }
//}
//
//function uploadSongPush(){
//    // Add the current song details to the songs array
//    const songTitle = document.getElementById('song-title').value;
//    const songDescription = document.getElementById('song-description').value;
//    const songTags = selectedTags; // Use the previously selected tags
//
//    // Create an object to hold the song information
//    const songInfo = {
//        title: songTitle,
//        description: songDescription,
//        tags: songTags
//    };
//
//    songsArray.push(songInfo); // Add the song information to the array
//}
//
//// Function to add more songs and navigate back to song input step
//function addMoreSongs() {
//    // Reset the song input fields for the next song
//    document.getElementById('song-title').value = '';
//    document.getElementById('song-description').value = '';
//    document.getElementById('file-upload').value = '';
//
//    // Hide the step
//    document.getElementById('submit-step').classList.remove('visible');
//    document.getElementById('tag-step').classList.remove('visible');
//
//    // Show the song input step
//    document.getElementById('group-song-step').classList.add('visible');
//
//    // Re-enable input fields in the song input step
//    const songInputElements = document.querySelectorAll('#group-song-step input, #group-song-step textarea');
//    songInputElements.forEach(element => {
//    element.disabled = false; // Enable input
//    });
//
//    // Clear the selected tags if desired
//    selectedTags = [];
//    document.getElementById('tag-list').innerHTML = ''; // Clear the displayed selected tags
//    enableNextButton(); // Ensure the next button is correctly enabled/disabled
//}
//
//
//// Function to reset form and clear selected tags
//function resetForm() {
//    const steps = ['type-step', 'group-info-step', 'group-song-step', 'tag-step', 'submit-step'];
//    steps.forEach(stepId => {
//        const stepElement = document.getElementById(stepId);
//        stepElement.classList.remove('visible');  // Hide all steps
//        stepElement.querySelectorAll('input, textarea, select').forEach(element => {
//            element.value = '';  // Clear input fields
//            element.disabled = false;  // Enable input fields
//        });
//    });
//
//    // Clear the selected tags
//    selectedTags = [];
//    document.getElementById('tag-list').innerHTML = '';  // Clear the displayed selected tags
//
//    // Show the first step and disable the upload button
//    document.getElementById('type-step').classList.add('visible');
//    document.getElementById('upload-button').disabled = true;
//
//    // Ensure "Next" button is disabled after reset
//    enableNextButton();
//}
//
//
//// Function to enable the "Next" button if tags are selected
//function enableNextButton() {
//    const nextButton = document.getElementById('next-step');
//    if (selectedTags.length > 0) {
//        nextButton.disabled = false;  // Enable button if tags are selected
//    } else {
//        nextButton.disabled = true;  // Keep disabled if no tags are selected
//    }
//}
//
//function renderSongs() {
//    const songInfoList = document.getElementById('song-info-list');
//    songInfoList.innerHTML = ''; // 기존 내용을 초기화
//
//    if (songsArray.length === 0) {
//        songInfoList.innerHTML = '<p>추가된 곡이 없습니다.</p>';
//        return;
//    }
//
//    songsArray.forEach((song, index) => {
//        const songDiv = document.createElement('div');
//        songDiv.classList.add('song-info');
//
//        songDiv.innerHTML = `
//                    <div class="test">
//                        <h3>설명 : ${song.description || '없음'}</h3>
//                        <p>제목 : ${song.title}</p>
//                        <span>태그 : ${song.tags.join(', ') || '없음'}</span>
//                        <button class="delete-song-btn" value="${index}">X</button>
//                    </div>
//        `;
//
//        // 이벤트 리스너 추가
//        const deleteButton = songDiv.querySelector('.delete-song-btn');
//        deleteButton.addEventListener('click', () => deleteSong(index));
//
//        songInfoList.appendChild(songDiv);
//    });
//}
//
//function deleteSong(index) {
//    songsArray.splice(index, 1);
//    renderSongs();
//}
//
//function confirm(){
//    // Add the current song details to the songs array
//    const groupTitle = document.getElementById('group-name').value;
//    const groupImage= document.getElementById('group-image').value;
//    const groupDescription = document.getElementById('group-description').value;
//
//    console.log("최종 앨범 정보 : "+groupTitle+", "+groupImage+", "+groupDescription);
//    console.log("최종 곡 정보 : "+songsArray);
//}