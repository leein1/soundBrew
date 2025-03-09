export function renderIndex() {
    const container = document.getElementById('content-body');
    container.innerHTML = '';

    const renderHTML = `

        <!-- 1. 팀원 2명 사진 (팀원 섹션) -->
        <div id="project-index">
        <h1>SoundBrew 프로젝트 포트폴리오</h1>
        <section id="team">
            <h2>Team Members</h2>
            <div class="team-members">
                <div class="member">
                    <img src="../images/user-test-01.jpeg" alt="Team Member 1">
                    <p>이인원</p>
                </div>
                <div class="member">
                    <img src="../images/user-test-01.jpeg" alt="Team Member 2">
                    <p>경동훈</p>
                </div>
            </div>
        </section>

        <!-- 1. 추가 이미지 섹션 2개 (총 4개 중 팀원 2개 외 추가) -->
        <section id="additional-images-1">
            <h2>Additional Image 1</h2>
            <div class="additional-images">
                <img src="/images/additional1.jpg" alt="Additional Image 1">
            </div>
        </section>
        <section id="additional-images-2">
            <h2>Additional Image 2</h2>
            <div class="additional-images">
                <img src="/images/additional2.jpg" alt="Additional Image 2">
            </div>
        </section>

        <!-- 2. 프로젝트 설명 섹션 -->
        <section id="description">
            <h2>Project Description</h2>
            <p>
                이 프로젝트는 포트폴리오로서 인사를 하기 위한 메인 페이지입니다.
                프로젝트의 간략한 소개 및 기술 문서를 확인할 수 있으며, 향후 다양한 기능을 추가할 예정입니다.
            </p>
        </section>

        <!-- 3. 깃허브 소스코드 링크 섹션 -->
        <section id="github">
            <h2>GitHub Source Code</h2>
            <a href="https://github.com/yourproject" target="_blank">View on GitHub</a>
        </section>

        <!-- 4. 노션 프로젝트 기술문서 링크 섹션 -->
        <section id="notion">
            <h2>Notion Project Documentation</h2>
            <a href="https://notion.so/yourprojectdoc" target="_blank">View Documentation on Notion</a>
        </section>

        <!-- 5. /sounds/tracks 로 이동하는 바로가기 -->
        <section id="navigation">
            <h2>Navigate to Sounds</h2>
            <button onclick="router.navigate('/sounds/tracks')">Go to /sounds/tracks</button>
        </section>
        </div>
    `;

    container.innerHTML = renderHTML;
}
