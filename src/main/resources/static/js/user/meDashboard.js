import { axiosGet } from '/js/fetch/standardAxios.js';
import { router } from '/js/router.js';

// 이 모듈에서만 사용할 데이터 캐시
let cachedStats = null;
let cachedStatsTags = null;

// 대시보드의 HTML을 렌더링하는 함수 (me 전용)
export function renderMeDashboard() {
  const container = document.getElementById('content-body');
  if (!container) return;

  container.innerHTML = `
    <div class="in-body">
      <!-- 드롭다운 메뉴 영역 -->
      <div id="chart-selector-container"></div>
      <!-- 통계 영역 -->
      <div id="question-list" class="question-list">
        <div class="statistics-section">
          <!-- 그룹1: 내 통계 [음원 관련 업로드, 판매] -->
          <div id="group1-section" class="category-section">
            <div class="chart-group-container">
              <div class="chart-container">
                <canvas id="soundDoughnutChart"></canvas>
              </div>
              <div class="chart-container">
                <canvas id="topSellingArtistsChart"></canvas>
              </div>
              <div class="chart-container">
                <canvas id="topSellingMusicChart"></canvas>
              </div>
              <div class="chart-container">
                <canvas id="topUploadArtistsChart"></canvas>
              </div>
            </div>
          </div>
          <!-- 그룹2: 사용 태그 통계 -->
          <div id="group2-section" class="category-section">
            <div class="chart-group-container">
              <div class="chart-container">
                <canvas id="instrumentBarChart"></canvas>
              </div>
              <div class="chart-container">
                <canvas id="moodBarChart"></canvas>
              </div>
              <div class="chart-container">
                <canvas id="genreBarChart"></canvas>
              </div>
            </div>
          </div>
          <!--<div id="group3-section" class="category-section">
            <div class="chart-group-container">
              <div class="chart-container">
                <canvas id="recentChangeSubscriptionTable"></canvas>
              </div>
            </div>
          </div>-->
        </div>
      </div>
      <!-- 수정페이지 영역 (기본적으로 숨김) -->
      <div class="frequently-list" style="display:none;">
        <li id="upload-sound">
          <span class="main-text">음원 업로드</span><br>
          <span class="sub-text">Upload Albums, Tracks, Tags</span>
          <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg" alt="chevron">
        </li>
        <li id="sound-users">
          <span class="main-text">내 음원 정보 수정</span><br>
          <span class="sub-text">Manage Albums, Tracks, Tags</span>
          <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg" alt="chevron">
        </li>
        <li id="info-users">
          <span class="main-text">내 정보 수정</span><br>
          <span class="sub-text">Manage My Info</span>
          <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg" alt="chevron">
        </li>
        <li id="subscription-users">
          <span class="main-text">내 정보 수정</span><br>
          <span class="sub-text">Manage My Info</span>
          <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg" alt="chevron">
        </li>
      </div>
    </div>
  `;

  // 드롭다운 메뉴 렌더링
  renderInfoSort();
}

// 드롭다운 메뉴 렌더링 함수 (me 전용)
export function renderInfoSort() {
  const container = document.getElementById('chart-selector-container');
  if (!container) return;

  const item = document.createElement('div');
  item.classList.add('music-sort');

  // 드롭다운 항목을 2개의 그룹과 수정페이지 항목으로 구성
  item.innerHTML = `
    <div class="sort-01">
      <span class="music-sort-left" id="sortKeywordMeStatistic">
        <img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정보 전환" id="sortIcon">
        통계 선택
      </span>
      <!-- 드롭다운 메뉴 -->
      <div class="music-sort-menu" id="musicSortMenuMeStatistic">
        <ul>
          <li data-category="group1">업로드/다운로드 통계</li>
          <li data-category="group2">사용 태그 통계</li>
          <!--<li data-category="group3">구독제 변경 조회</li>-->
          <li data-chart="editPage">수정페이지</li>
        </ul>
      </div>
    </div>
  `;

  container.appendChild(item);
  setupInfoDropdownEvents();

  const frequentlyList = document.querySelector('.frequently-list');
      // 이벤트 위임: .frequently-list에 클릭 이벤트 등록
      frequentlyList.addEventListener('click', (event) => {
        // 클릭된 요소가 li 내부의 span/img일 수 있으므로, closest('li')로 li 요소를 찾음
        const clickedLi = event.target.closest('li');
        if (!clickedLi) return; // li가 아니면 무시

        // li의 id에 따라 분기 처리
        switch (clickedLi.id) {
          case 'upload-sound':
            router.navigate('/me/sounds/upload');
            break;

          case 'sound-users':
            router.navigate('/me/sounds/albums');
            break;

          case 'info-users':
            router.navigate('마이인포 변경 연결');
            break;

          case 'subscription-users':
            router.navigate('/구독제 변경 연결');
            break;

          default:
            console.log('정의되지 않은 li 클릭');
            break;
        }
      });
}

// 드롭다운 메뉴 이벤트 설정 함수 (me 전용)
function setupInfoDropdownEvents() {
  const sortKeyword = document.getElementById('sortKeywordMeStatistic');
  const menu = document.getElementById('musicSortMenuMeStatistic');

  // 토글 아이콘 클릭 시 드롭다운 메뉴 표시/숨김
  sortKeyword.addEventListener('click', () => {
    menu.classList.toggle('visible');
  });

  // 각 메뉴 항목 클릭 시 이벤트 처리
  menu.querySelectorAll('li').forEach(li => {
    li.addEventListener('click', async () => {
      // 드롭다운 닫기
      menu.classList.remove('visible');

      // 카테고리 혹은 수정페이지 선택 분기
      const category = li.getAttribute('data-category');
      const chartType = li.getAttribute('data-chart');
      if (chartType === 'editPage') {
        // 수정페이지 선택 시: 통계 영역 숨기고 수정 메뉴 표시
        document.querySelectorAll('.category-section').forEach(sec => sec.style.display = 'none');
        const frequentlyList = document.querySelector('.frequently-list');
        if (frequentlyList) {
          frequentlyList.style.display = 'block';
        }
        // 업데이트된 드롭다운 텍스트
        sortKeyword.innerHTML = `<img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정보 전환" id="sortIcon">수정페이지`;
        return;
      } else {
        // 수정페이지가 아닌 경우 수정 메뉴 숨김
        const frequentlyList = document.querySelector('.frequently-list');
        if (frequentlyList) {
          frequentlyList.style.display = 'none';
        }
      }

      // 선택된 항목의 텍스트로 토글 영역 업데이트 (아이콘 유지)
      sortKeyword.innerHTML = `<img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정보 전환" id="sortIcon">` + li.textContent;

      // active 스타일 적용
      menu.querySelectorAll('li').forEach(item => item.classList.remove('active'));
      li.classList.add('active');

      // 선택된 옵션에 따라 처리 (캐싱된 데이터를 사용)
      if (category === 'group1') {
        ChartModule.showCategorySection('group1');
        await ChartModule.createMySoundDoughnutChart(cachedStats);
        await ChartModule.createTopSellingArtistsChart(cachedStats);
        await ChartModule.createMyMusicChart(cachedStats);
        await ChartModule.createTopUploadArtistsChart(cachedStats);
      } else if (category === 'group2') {
        ChartModule.showCategorySection('group2');
        await ChartModule.createInstrumentBarChart();
        await ChartModule.createMoodBarChart();
        await ChartModule.createGenreBarChart();
      }
//      else if (category === 'group3') {
//        ChartModule.showCategorySection('group3');
//        // 구독제 변경 조회 관련 차트 함수 호출 (예: createSubscriptionChart)
//        // await ChartModule.createSubscriptionChart(cachedStats);
//      }
    });
  });
}

// 차트 모듈: me 통계를 위한 차트 함수들 (태그 통계 함수 포함)
const ChartModule = (() => {
  const charts = {};

  // 모든 카테고리 영역 숨기기
  function hideAllCategorySections() {
    document.querySelectorAll('.category-section').forEach(section => {
      section.style.display = 'none';
    });
  }

  // 지정된 카테고리 영역 보이기
  function showCategorySection(group) {
    hideAllCategorySections();
    const section = document.getElementById(group + '-section');
    if (section) section.style.display = 'block';
  }

  // 1. 나의 음원/앨범 갯수 (도넛 차트)
  async function createMySoundDoughnutChart(sharedStats) {
    try {
      const canvas = document.getElementById('soundDoughnutChart');
      if (charts["soundDoughnutChart"]) {
        charts["soundDoughnutChart"].destroy();
        delete charts["soundDoughnutChart"];
      }
      const ctx = canvas.getContext('2d');
      charts["soundDoughnutChart"] = new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: ['앨범', '음원'],
          datasets: [{
            data: [sharedStats.dto.musicCountTotal, sharedStats.dto.albumCountTotal],
            backgroundColor: ['rgba(255, 159, 64, 0.6)', 'rgba(54, 162, 235, 0.6)'],
            borderColor: ['rgba(255, 159, 64, 1)', 'rgba(54, 162, 235, 1)'],
            borderWidth: 1
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, animation: false }
      });
    } catch (error) {
      console.error("My Sound Doughnut Chart 생성 오류:", error);
    }
  }

  // 2. 내가 만든 음원 (바 차트) - 기간별: 일, 주, 월, 총
  async function createMyMusicChart(sharedStats) {
    try {
      const canvas = document.getElementById('topSellingMusicChart');
      if (charts["topSellingMusicChart"]) {
        charts["topSellingMusicChart"].destroy();
        delete charts["topSellingMusicChart"];
      }
      const ctx = canvas.getContext('2d');
      charts["topSellingMusicChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['일', '주', '월', '총'],
          datasets: [{
            label: '업로드 통계',
            data: [
              sharedStats.dto.musicCountDay,
              sharedStats.dto.musicCountWeek,
              sharedStats.dto.musicCountMonth,
              sharedStats.dto.musicCountTotal
            ],
            backgroundColor: 'rgba(75, 192, 192, 0.6)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
      });
    } catch (error) {
      console.error("My Music Chart 생성 오류:", error);
    }
  }

  // 3. 내가 판 음원 횟수 (바 차트) - 기간별: 일, 주, 월, 총
  async function createTopSellingArtistsChart(sharedStats) {
    try {
      const canvas = document.getElementById('topSellingArtistsChart');
      if (charts["topSellingArtistsChart"]) {
        charts["topSellingArtistsChart"].destroy();
        delete charts["topSellingArtistsChart"];
      }
      const ctx = canvas.getContext('2d');
      charts["topSellingArtistsChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['일', '주', '월', '총'],
          datasets: [{
            label: '음원 다운로드(판매) 통계',
            data: [
              sharedStats.dto.downloadsDay,
              sharedStats.dto.downloadsWeek,
              sharedStats.dto.downloadsMonth,
              sharedStats.dto.totalDownloads
            ],
            backgroundColor: 'rgba(255, 99, 132, 0.6)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: { y: { beginAtZero: true } }
        }
      });
    } catch (error) {
      console.error("Top Selling Artists Chart 생성 오류:", error);
    }
  }

  // 4. 가장 많이 다운로드된 음원 (바 차트)
  async function createTopUploadArtistsChart(sharedStats) {
    try {
      const canvas = document.getElementById('topUploadArtistsChart');
      if (charts["topUploadArtistsChart"]) {
        charts["topUploadArtistsChart"].destroy();
        delete charts["topUploadArtistsChart"];
      }
      const dto = sharedStats.dto;
      const topTrackTitles = dto.topTrackTitles;
      const topTrackDownloadCounts = dto.topTrackDownloadCounts;

      if (!topTrackTitles || topTrackTitles.length === 0) {
        console.warn("다운로드된 음원이 없습니다.");
        return;
      }

      const ctx = canvas.getContext('2d');
      charts["topUploadArtistsChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: topTrackTitles,
          datasets: [{
            label: '인기곡 통계(나의)',
            data: topTrackDownloadCounts,
            backgroundColor: 'rgba(255, 99, 132, 0.6)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: { y: { beginAtZero: true } }
        }
      });
    } catch (error) {
      console.error("Top Downloaded Tracks Chart 생성 오류:", error);
    }
  }

  // 5. 태그 통계 차트

  // 5-1. 악기 태그 사용 통계 차트
  async function createInstrumentBarChart() {
    try {
      const canvas = document.getElementById('instrumentBarChart');
      if (charts["instrumentBarChart"]) {
        charts["instrumentBarChart"].destroy();
        delete charts["instrumentBarChart"];
      }
      const data = cachedStatsTags.dto.instrumentUsageCount;
      const ctx = canvas.getContext('2d');
      charts["instrumentBarChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: data.map(item => item.tagName),
          datasets: [{
            label: '악기 태그 사용 통계',
            data: data.map(item => item.count),
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: { y: { beginAtZero: true } }
        }
      });
    } catch (error) {
      console.error("Instrument Bar Chart 생성 오류:", error);
    }
  }

  // 5-2. 무드 태그 사용 통계 차트
  async function createMoodBarChart() {
    try {
      const canvas = document.getElementById('moodBarChart');
      if (charts["moodBarChart"]) {
        charts["moodBarChart"].destroy();
        delete charts["moodBarChart"];
      }
      const data = cachedStatsTags.dto.moodUsageCount;
      const ctx = canvas.getContext('2d');
      charts["moodBarChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: data.map(item => item.tagName),
          datasets: [{
            label: '무드 태그 사용 통계',
            data: data.map(item => item.count),
            backgroundColor: 'rgba(255, 99, 132, 0.6)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: { y: { beginAtZero: true } }
        }
      });
    } catch (error) {
      console.error("Mood Bar Chart 생성 오류:", error);
    }
  }

  // 5-3. 장르 태그 사용 통계 차트
  async function createGenreBarChart() {
    try {
      const canvas = document.getElementById('genreBarChart');
      if (charts["genreBarChart"]) {
        charts["genreBarChart"].destroy();
        delete charts["genreBarChart"];
      }
      const data = cachedStatsTags.dto.genreUsageCount;
      const ctx = canvas.getContext('2d');
      charts["genreBarChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: data.map(item => item.tagName),
          datasets: [{
            label: '장르 태그 사용 통계',
            data: data.map(item => item.count),
            backgroundColor: 'rgba(75, 192, 192, 0.6)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: { y: { beginAtZero: true } }
        }
      });
    } catch (error) {
      console.error("Genre Bar Chart 생성 오류:", error);
    }
  }

  // 창 크기 변경 시 차트 리사이즈
  function resizeCharts() {
    Object.values(charts).forEach(chart => chart.resize());
  }

  return {
    charts,
    showCategorySection,
    resizeCharts,
    createMySoundDoughnutChart,
    createMyMusicChart,
    createTopSellingArtistsChart,
    createTopUploadArtistsChart,
    createInstrumentBarChart,
    createMoodBarChart,
    createGenreBarChart
  };
})();

// 초기화 함수: 렌더링 후 차트 생성 및 이벤트 리스너 등록 (me 전용)
export async function initMeDashboard(sharedStats, tagsStats) {
  // 라우터에서 전달된 데이터를 모듈 스코프 변수에 저장
  cachedStats = sharedStats;
  cachedStatsTags = tagsStats;

  renderMeDashboard();

  // 기본: "내 통계" (그룹1) 영역 활성화 후 차트 생성 (캐시된 데이터 사용)
  ChartModule.showCategorySection('group1');
  await ChartModule.createMySoundDoughnutChart(cachedStats);
  await ChartModule.createTopSellingArtistsChart(cachedStats);
  await ChartModule.createMyMusicChart(cachedStats);
  await ChartModule.createTopUploadArtistsChart(cachedStats);

  window.addEventListener('resize', ChartModule.resizeCharts);
}
