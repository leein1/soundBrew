import {router} from '/js/router.js';

export function renderIndex() {
    const container = document.getElementById('content-body');
    container.innerHTML = '';

    const renderHTML = `

            <!-- 1. 팀원 2명 사진 (팀원 섹션) -->
            <div id="project-index">
                <h1>SoundBrew 프로젝트 웹페이지</h1>
                    <div id="top-section">
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

                        <!-- 2. 프로젝트 설명 섹션 -->
                        <section id="description">
                            <h2>Project Description</h2>
                            <p>
                                본 프로젝트는 음원 정보를 찾고 업로드하며 청취하는 과정에서 겪었던 다양한 경험을 바탕으로,<br>
                                편리하게 음원을 검색하고 공유할 수 있도록 만들어졌습니다.<br><br>
                                태그 버튼으로 간편한 검색, 특정 음원 재생 버튼을 통한 즉각적인 청취,<br>
                                그리고 사이드 바를 활용한 개인 정보 관리 등 여러 기능들을 직접 체험해 보세요.
                            </p>
                        </section>
                    </div> <!-- 닫히지 않았던 #top-section의 닫힘 태그 추가 -->

            <!-- 1. 추가 이미지 섹션 2개 (총 4개 중 팀원 2개 외 추가) -->
            <section id="additional-images-1">
                <h2>ex) 데스크탑 프레임에 우리 사이트 이미지</h2>
                <div class="additional-images">
                    <img src="/images/additional1.jpg" alt="Additional Image 1">
                </div>
            </section>
            <section id="additional-images-2">
                <h2>ex) 스마트폰 프레임에 우리 사이트 이미지</h2>
                <div class="additional-images">
                    <img src="/images/additional2.jpg" alt="Additional Image 2">
                </div>
            </section>

            <!-- 5. /sounds/tracks 로 이동하는 바로가기 -->
            <section id="navigation">
                <h2>Navigate to Sounds</h2>
                <button id="go-to-soundbrew">Go, SoundBrew</button>
            </section>

            <!-- 3. 깃허브 소스코드 링크 섹션 -->
            <section id="github">
                <h2>SoundBrew 깃허브 소스코드</h2>
                <a href="https://github.com/leein1/soundBrew" target="_blank">SoundBrew 깃허브 소스코드</a>
            </section>

            <!-- 4. 노션 프로젝트 기술문서 링크 섹션 -->
            <section id="notion">
                <h2>SoundBrew 프로젝트 기술 문서</h2>
                <a href="https://leeinwon.notion.site/1aea7464bbfc80ec84f0d3b7c0a5a991" target="_blank">SoundBrew 프로젝트 기술 문서</a>
            </section>

            <section id="inwon-portfolio">
                <h2>이인원 포트폴리오</h2>
                <a href="https://leeinwon.notion.site/1aea7464bbfc80ec84f0d3b7c0a5a991" target="_blank">이인원 포트폴리오</a>
            </section>

            <section id="hun-portfolio">
                <h2>경동훈 포트폴리오</h2>
                <a href="https://leeinwon.notion.site/1aea7464bbfc80ec84f0d3b7c0a5a991" target="_blank">경동훈 포트폴리오</a>
            </section>
        </div> <!-- 닫히지 않았던 #project-index의 닫힘 태그 추가 -->
    `;

    container.innerHTML = renderHTML;

    const goSoundBrew = document.getElementById('go-to-soundbrew');
    if (goSoundBrew) {
        goSoundBrew.addEventListener("click", () => {
            router.navigate("/sounds/tracks");
        });
    }
}
