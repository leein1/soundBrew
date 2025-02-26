import { axiosGet } from '/js/fetch/standardAxios.js';
import { router } from '/js/router.js';

// 대시보드의 HTML을 렌더링하는 함수
export function renderDashboard() {
  const container = document.getElementById('content-body');
  if (!container) return;

  container.innerHTML = `
    <div class="in-body">
      <!-- 드롭다운 메뉴 영역 -->
      <div id="chart-selector-container"></div>
      <!-- 통계 및 수정페이지 영역 -->
      <div id="question-list" class="question-list">
        <!-- 좌측: 통계 영역 -->
        <div class="statistics-section">
          <!-- 카테고리별 차트 영역 -->
          <div id="music-section" class="category-section">
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
          <div id="tag-section" class="category-section">
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
          <div id="user-section" class="category-section">
            <div class="chart-group-container">
              <div class="chart-container">
                <canvas id="userPieChart"></canvas>
              </div>
              <div class="chart-container">
                <canvas id="subscriptionChart"></canvas>
              </div>
              <div class="chart-container">
                <canvas id="recentChangeSubscriptionTable"></canvas>
              </div>
            </div>
          </div>
        </div>
        <!-- 우측: 수정페이지 영역 (자주 사용하는 기능 메뉴) -->
        <div class="frequently-list">
          <li id="admin-sound">
            <span class="main-text">앨범 정보 확인</span><br>
            <span class="sub-text">Albums, Tracks, Tags, Verify</span>
            <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg" alt="chevron">
          </li>
          <li id="admin-users">
            <span class="main-text">유저 정보 확인</span><br>
            <span class="sub-text">Users, Subscription, User-Subscription</span>
            <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg" alt="chevron">
          </li>
        </div>
      </div>
    </div>
  `;

  // 드롭다운 메뉴 렌더링
  renderInfoSort();
}


// 드롭다운 메뉴 렌더링 함수
export function renderInfoSort() {
  const container = document.getElementById('chart-selector-container');
  if (!container) return;

  const item = document.createElement('div');
  item.classList.add('music-sort'); // 기존 스타일 재사용

  item.innerHTML = `
    <div class="sort-01">
      <span class="music-sort-left" id="sortKeywordStatistic">
        <img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정보 전환" id="sortIcon">정보 전환
      </span>
      <!-- 드롭다운 메뉴 -->
      <div class="music-sort-menu" id="musicSortMenuStatistic">
        <ul>
          <li data-category="music">음원관련 통계</li>
          <li data-category="tag">태그관련 통계</li>
          <li data-category="user">유저관련 통계</li>
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
        case 'admin-sound':
          router.navigate('/admin/albums');
          break;

        case 'admin-users':
          router.navigate('/admin/users');
          break;

        default:
          console.log('정의되지 않은 li 클릭');
          break;
      }
    });
}

// 드롭다운 메뉴 이벤트 설정 함수
function setupInfoDropdownEvents() {
  const sortKeyword = document.getElementById('sortKeywordStatistic');
  const menu = document.getElementById('musicSortMenuStatistic');

  // 정렬 아이콘 클릭 시 드롭다운 토글 (CSS에서 .visible에 대한 스타일 정의 필요)
  sortKeyword.addEventListener('click', () => {
    menu.classList.toggle('visible');
  });

  // 메뉴 아이템 클릭 시 이벤트 처리
  menu.querySelectorAll('li').forEach(li => {
    li.addEventListener('click', async () => {
      // 드롭다운 닫기
      menu.classList.remove('visible');

      // active 스타일 적용 (필요에 따라 CSS 수정)
      menu.querySelectorAll('li').forEach(item => item.classList.remove('active'));
      li.classList.add('active');

      // 카테고리 혹은 수정페이지 선택 분기
      const category = li.getAttribute('data-category');
      const chartType = li.getAttribute('data-chart');
      if (chartType === 'editPage') {
        // 수정페이지: 통계 영역 숨기고 수정 메뉴 표시
        document.querySelectorAll('.category-section').forEach(sec => sec.style.display = 'none');
        document.querySelector('.frequently-list').style.display = 'block';
        return;
      } else {
        document.querySelector('.frequently-list').style.display = 'none';
      }

      // 선택된 카테고리 기준 차트 영역 표시 및 차트 생성
      ChartModule.showCategorySection(category);
      if (category === 'music') {
        await ChartModule.createSoundDoughnutChart();
        await ChartModule.createTopSellingArtistsChart();
        await ChartModule.createTopSellingMusicChart();
        await ChartModule.createTopUploadArtistsChart();
      } else if (category === 'tag') {
        await ChartModule.createInstrumentBarChart();
        await ChartModule.createMoodBarChart();
        await ChartModule.createGenreBarChart();
      } else if (category === 'user') {
        await ChartModule.createUserPieChart();
        await ChartModule.createSubscriptionChart();
      }
    });
  });
}

// 차트 모듈: 각 차트를 생성하고 관리하는 기능들을 캡슐화
const ChartModule = (() => {
  const charts = {};

  // 모든 카테고리 영역 숨기기
  function hideAllCategorySections() {
    document.querySelectorAll('.category-section').forEach(section => {
      section.style.display = 'none';
    });
  }

  // 지정된 카테고리 영역 보이기
  function showCategorySection(category) {
    hideAllCategorySections();
    const section = document.getElementById(category + '-section');
    if (section) section.style.display = 'block';
  }

  async function createSoundDoughnutChart() {
    try {
      const canvas = document.getElementById('soundDoughnutChart');
      if (charts["soundDoughnutChart"]) {
        charts["soundDoughnutChart"].destroy();
        delete charts["soundDoughnutChart"];
      }
      const soundsStats = await axiosGet({ endpoint: '/api/statistic/sounds/stats' });
      const ctx = canvas.getContext('2d');
      charts["soundDoughnutChart"] = new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: ['Total Albums', 'Total Musics'],
          datasets: [{
            data: [soundsStats.dto.totalAlbums, soundsStats.dto.totalMusics],
            backgroundColor: ['rgba(255, 159, 64, 0.6)', 'rgba(54, 162, 235, 0.6)'],
            borderColor: ['rgba(255, 159, 64, 1)', 'rgba(54, 162, 235, 1)'],
            borderWidth: 1
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, animation: false }
      });
    } catch (error) {
      console.error("Sound Doughnut Chart 생성 오류:", error);
    }
  }

  async function createTopSellingMusicChart() {
    try {
      const canvas = document.getElementById('topSellingMusicChart');
      if (charts["topSellingMusicChart"]) {
        charts["topSellingMusicChart"].destroy();
        delete charts["topSellingMusicChart"];
      }
      const soundsStats = await axiosGet({ endpoint: '/api/statistic/sounds/stats' });
      const data = soundsStats.dto.topSellingMusic;
      const ctx = canvas.getContext('2d');
      charts["topSellingMusicChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: data.map(item => item.title),
          datasets: [{
            label: '음원 판매',
            data: data.map(item => item.count),
            backgroundColor: 'rgba(75, 192, 192, 0.6)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
      });
    } catch (error) {
      console.error("Top Selling Music Chart 생성 오류:", error);
    }
  }

  async function createTopSellingArtistsChart() {
    try {
      const canvas = document.getElementById('topSellingArtistsChart');
      if (charts["topSellingArtistsChart"]) {
        charts["topSellingArtistsChart"].destroy();
        delete charts["topSellingArtistsChart"];
      }
      const soundsStats = await axiosGet({ endpoint: '/api/statistic/sounds/stats' });
      const data = soundsStats.dto.topArtistsBySales;
      const ctx = canvas.getContext('2d');
      charts["topSellingArtistsChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: data.map(item => item.nickname),
          datasets: [{
            label: '음원 판매 아티스트',
            data: data.map(item => item.count),
            backgroundColor: 'rgba(255, 99, 132, 0.6)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
      });
    } catch (error) {
      console.error("Top Selling Artists Chart 생성 오류:", error);
    }
  }

  async function createTopUploadArtistsChart() {
    try {
      const canvas = document.getElementById('topUploadArtistsChart');
      if (charts["topUploadArtistsChart"]) {
        charts["topUploadArtistsChart"].destroy();
        delete charts["topUploadArtistsChart"];
      }
      const soundsStats = await axiosGet({ endpoint: '/api/statistic/sounds/stats' });
      const data = soundsStats.dto.topArtistsByUploads;
      const ctx = canvas.getContext('2d');
      charts["topUploadArtistsChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: data.map(item => item.nickname),
          datasets: [{
            label: '음원 업로드 아티스트',
            data: data.map(item => item.count),
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
      });
    } catch (error) {
      console.error("Top Upload Artists Chart 생성 오류:", error);
    }
  }

  async function createInstrumentBarChart() {
    try {
      const canvas = document.getElementById('instrumentBarChart');
      if (charts["instrumentBarChart"]) {
        charts["instrumentBarChart"].destroy();
        delete charts["instrumentBarChart"];
      }
      const tagsStats = await axiosGet({ endpoint: '/api/statistic/tags/stats' });
      const data = tagsStats.dto.instrumentUsageCount;
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
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
      });
    } catch (error) {
      console.error("Instrument Bar Chart 생성 오류:", error);
    }
  }

  async function createMoodBarChart() {
    try {
      const canvas = document.getElementById('moodBarChart');
      if (charts["moodBarChart"]) {
        charts["moodBarChart"].destroy();
        delete charts["moodBarChart"];
      }
      const tagsStats = await axiosGet({ endpoint: '/api/statistic/tags/stats' });
      const data = tagsStats.dto.moodUsageCount;
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
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
      });
    } catch (error) {
      console.error("Mood Bar Chart 생성 오류:", error);
    }
  }

  async function createGenreBarChart() {
    try {
      const canvas = document.getElementById('genreBarChart');
      if (charts["genreBarChart"]) {
        charts["genreBarChart"].destroy();
        delete charts["genreBarChart"];
      }
      const tagsStats = await axiosGet({ endpoint: '/api/statistic/tags/stats' });
      const data = tagsStats.dto.genreUsageCount;
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
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
      });
    } catch (error) {
      console.error("Genre Bar Chart 생성 오류:", error);
    }
  }

  async function createUserPieChart() {
    try {
      const canvas = document.getElementById('userPieChart');
      if (charts["userPieChart"]) {
        charts["userPieChart"].destroy();
        delete charts["userPieChart"];
      }
      const usersStat = await axiosGet({ endpoint: '/api/statistic/users/stats' });
      const ctx = canvas.getContext('2d');
      charts["userPieChart"] = new Chart(ctx, {
        type: 'pie',
        data: {
          labels: ['전체 회원','신규 회원(today)','신규 회원(week)','신규 회원(Month)','구독제 가입 유저','이메일 인증 유저'],
          datasets: [{
            data: [
              usersStat.dto.totalUsers,
              usersStat.dto.newUsersToday,
              usersStat.dto.nwUsersWeek,
              usersStat.dto.newUsersMonth,
              usersStat.dto.subscribedUsers,
              usersStat.dto.verifiedUsers
            ],
            backgroundColor: [
              'rgba(255, 99, 132, 0.6)',
              'rgba(54, 162, 235, 0.6)',
              'rgba(255, 206, 86, 0.6)',
              'rgba(75, 192, 192, 0.6)',
              'rgba(153, 102, 255, 0.6)',
              'rgba(255, 159, 64, 0.6)'
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)',
              'rgba(54, 162, 235, 1)',
              'rgba(255, 206, 86, 1)',
              'rgba(75, 192, 192, 1)',
              'rgba(153, 102, 255, 1)',
              'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1
          }]
        },
        options: { responsive: true, maintainAspectRatio: false }
      });
    } catch (error) {
      console.error("User Pie Chart 생성 오류:", error);
    }
  }

  async function createSubscriptionChart() {
    try {
      const canvas = document.getElementById('subscriptionChart');
      if (charts["subscriptionChart"]) {
        charts["subscriptionChart"].destroy();
        delete charts["subscriptionChart"];
      }
      const userSubscriptionStats = await axiosGet({ endpoint: '/api/statistic/subscription/stats' });
      const data = userSubscriptionStats.dto.subscriptionStatisticDTO;
      const ctx = canvas.getContext('2d');
      charts["subscriptionChart"] = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: data.map(item => item.subscriptionName),
          datasets: [{
            label: '예상 월간 수익',
            data: data.map(item => item.monthlyRevenue),
            backgroundColor: 'rgba(255, 205, 86, 0.6)',
            borderColor: 'rgba(255, 205, 86, 1)',
            borderWidth: 1
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, animation: false, scales: { y: { beginAtZero: true } } }
      });
    } catch (error) {
      console.error("Subscription Chart 생성 오류:", error);
    }
  }

  function resizeCharts() {
    Object.values(charts).forEach(chart => chart.resize());
  }

  return {
    charts,
    showCategorySection,
    resizeCharts,
    createSoundDoughnutChart,
    createTopSellingMusicChart,
    createTopSellingArtistsChart,
    createTopUploadArtistsChart,
    createInstrumentBarChart,
    createMoodBarChart,
    createGenreBarChart,
    createUserPieChart,
    createSubscriptionChart
  };
})();

// 이벤트 리스너 설정
function setupEventListeners() {
  document.querySelectorAll('.chart-selector button').forEach(btn => {
    btn.addEventListener('click', async () => {
      // 모든 버튼의 active 클래스 제거 후 현재 버튼에 추가
      document.querySelectorAll('.chart-selector button').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');

      // 수정페이지 버튼 클릭 시 통계 영역 숨기고 수정 메뉴 표시
      if (btn.getAttribute('data-chart') === 'editPage') {
        document.querySelectorAll('.category-section').forEach(sec => sec.style.display = 'none');
        document.querySelector('.frequently-list').style.display = 'block';
        return;
      } else {
        document.querySelector('.frequently-list').style.display = 'none';
      }

      const category = btn.getAttribute('data-category');
      if (category) {
        ChartModule.showCategorySection(category);
        if (category === 'music') {
          await ChartModule.createSoundDoughnutChart();
          await ChartModule.createTopSellingArtistsChart();
          await ChartModule.createTopSellingMusicChart();
          await ChartModule.createTopUploadArtistsChart();
        } else if (category === 'tag') {
          await ChartModule.createInstrumentBarChart();
          await ChartModule.createMoodBarChart();
          await ChartModule.createGenreBarChart();
        } else if (category === 'user') {
          await ChartModule.createUserPieChart();
          await ChartModule.createSubscriptionChart();
        }
      }
    });
  });
}

// 초기화 함수: 렌더링 후 차트 생성 및 이벤트 리스너 등록
export async function initDashboard() {
  renderDashboard();
  setupEventListeners();

  // 기본: "음원관련 통계" 버튼 활성화 후 관련 차트 생성
  const defaultBtn = document.querySelector('button[data-category="music"]');
  if (defaultBtn) defaultBtn.classList.add('active');
  ChartModule.showCategorySection('music');
  await ChartModule.createSoundDoughnutChart();
  await ChartModule.createTopSellingArtistsChart();
  await ChartModule.createTopSellingMusicChart();
  await ChartModule.createTopUploadArtistsChart();

  window.addEventListener('resize', ChartModule.resizeCharts);
}