const form = document.getElementById("myForm");
const tagModal = document.getElementById("tag-modal");
const openModalBtn = document.querySelector(".open-modal");
const closeModalBtn = document.querySelector(".close-modal");
const tagList = document.getElementById("tag-list");
const tagSearch = document.getElementById("tag-search");
const selectedTags = document.getElementById("selected-tags");
const resetTagsBtn = document.querySelector(".reset-tags");

let allTags = [];

openModalBtn.addEventListener("click", openTagModal);
closeModalBtn.addEventListener("click", closeTagModal);
resetTagsBtn.addEventListener("click", resetTags);

function selectTag(tag, type) {
    if (!Array.from(selectedTags.children).some(li => li.textContent === tag)) {
        const selectedLi = document.createElement("li");
        selectedLi.textContent = tag;
        selectedTags.appendChild(selectedLi);

        const input = document.createElement("input");
        input.type = "text";
        input.name = `tagsDto.${type}`;
        input.value = tag;
        input.hidden = true;
        form.appendChild(input);
    }
}
                                                                                                                            async function openTagModal() {
                                                                                                                                tagModal.classList.remove("hidden");

                                                                                                                                try {
                                                                                                                                    const response = await axios.get('/api/admin/tags');
                                                                                                                                    const { dtoList } = response.data;

                                                                                                                                    allTags = [
                                                                                                                                        ...dtoList[0].instrument.map(tag => ({ tag, type: 'instrument' })),
                                                                                                                                        ...dtoList[0].mood.map(tag => ({ tag, type: 'mood' })),
                                                                                                                                        ...dtoList[0].genre.map(tag => ({ tag, type: 'genre' }))
                                                                                                                                    ].filter(item => item.tag);

                                                                                                                                    renderTagList(allTags);
                                                                                                                                } catch (error) {
                                                                                                                                    console.error("태그 데이터를 가져오는 중 오류 발생:", error);
                                                                                                                                }
                                                                                                                            }

                                                                                                                            function renderTagList(tags) {
                                                                                                                                tagList.innerHTML = "";

                                                                                                                                tags.forEach(({ tag, type }) => {
                                                                                                                                    const li = document.createElement("li");
                                                                                                                                    li.textContent = tag;
                                                                                                                                    li.addEventListener("click", () => selectTag(tag, type));
                                                                                                                                    tagList.appendChild(li);
                                                                                                                                });
                                                                                                                            }

function closeTagModal() {
    tagModal.classList.add("hidden");
}

function resetTags() {
    selectedTags.innerHTML = "";
    const inputs = document.querySelectorAll("input[name^='tagsDto.']");
    inputs.forEach(input => input.remove());
}
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        // 태그 검증 로직
        const hasInstrumentTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDto.instrument"));
        const hasMoodTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDto.mood"));
        const hasGenreTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDto.genre"));

        if (!hasInstrumentTag || !hasMoodTag || !hasGenreTag) {
            alert("태그를 하나 이상 선택해야 합니다: 악기, 무드, 장르 중 하나를 선택해주세요.");
            return; // 제출 중단
        }
        const jsonData = serializeFormToJSON(form);
        const { error, processedData } = inputHandler(jsonData, validationRules, processingRules, form);
    });