// CSS 로딩 상태를 관리할 Map
const cssRegistry = new Map();

export function loadExternalCSS(url) {
    // 이미 로딩 중이거나 로드가 완료된 경우, 기존 프로미스 반환
    if (cssRegistry.has(url)) {
        return cssRegistry.get(url).promise;
    }

    let canceled = false;
    const link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = url;
    link.type = "text/css";
    // 동적 로딩된 CSS임을 표시
    link.setAttribute("data-dynamic", "true");

    const promise = new Promise((resolve, reject) => {
        // 이미 DOM에 존재하면 바로 resolve
        if (document.querySelector(`link[data-dynamic][href="${url}"]`)) {
            resolve();
            return;
        }
        link.onload = () => {
            if (canceled) {
                reject(new Error(`CSS load cancelled: ${url}`));
            } else {
                resolve();
            }
        };
        link.onerror = () => reject(new Error(`Failed to load CSS: ${url}`));
        document.head.appendChild(link);
    }).finally(() => {
        // 완료되면 상태에서 제거
        cssRegistry.delete(url);
    });

    // 상태를 registry에 저장
    cssRegistry.set(url, {
        promise,
        link,
        cancel() {
            canceled = true;
            // 아직 로딩 중이라면 DOM에서 제거
            if (link.parentNode) {
                link.parentNode.removeChild(link);
            }
        }
    });

    return promise;
}

// 기존 그룹 배열 (필요에 따라 그룹별 관리 가능)
export const SoundTypeCSSFiles = [
    "/css/sound/music.css",
    "/css/sound/album.css",
    "/css/sound/sound.css",
    "/css/sound/album-list.css"
];

export const SoundManageTypeCSSFiles = [
    "/css/sound/manage-tags.css",
    "/css/user/user-admin.css",
    "/css/sound/admin-main.css"
];

export const SoundManageMainTypeCSSFiles = [
    "/css/sound/manage-main.css",
    "/css/sound/manage-albums.css",
    "/css/sound/manage-tags.css",
    "/css/sound/music-upload.css",
];

export const UserAdminTypeCSSFiles=[
    "/css/user/user-admin.css",
    "/css/user/myInfo.css",
    "/css/user/changepw.css",
    "/css/user/mySubscription.css",
    "/css/user/subscriptionPlan.css"
]

export const AdminStatisticTypeCSSFiles=[
    "/css/sound/admin-main.css"
]

export async function loadSoundTypeCSS() {
    // 모든 CSS가 로드될 때까지 기다립니다.
    await Promise.all(SoundTypeCSSFiles.map(url => loadExternalCSS(url)));
}

export async function loadSoundManageTypeCSS() {
    // 모든 CSS가 로드될 때까지 기다립니다.
    await Promise.all(SoundManageTypeCSSFiles.map(url => loadExternalCSS(url)));
}

export async function loadSoundManageMainTypeCSS() {
    // 모든 CSS가 로드될 때까지 기다립니다.
    await Promise.all(SoundManageMainTypeCSSFiles.map(url => loadExternalCSS(url)));
}

export async function loadUserAdminTypeCSS(){
    await Promise.all(UserAdminTypeCSSFiles.map(url => loadExternalCSS(url)));
}

export async function loadAdminStatisticTypeCSS(){
    await Promise.all(AdminStatisticTypeCSSFiles.map(url => loadExternalCSS(url)));
}
// 페이지 전환 시 동적 로딩된 모든 CSS를 한 번에 제거하는 함수
export function removeAllDynamicCSS() {
    const links = document.querySelectorAll('link[data-dynamic="true"]');
    links.forEach(link => {
        link.remove();
    });
    cssRegistry.clear();
    console.log("모든 동적 CSS가 제거되었습니다.");
}

// 현재 로드된 동적 CSS 중에서, allowedUrls 배열에 포함되지 않은 CSS만 제거하는 함수
export function updateDynamicCSS(allowedUrls) {
    const links = document.querySelectorAll('link[data-dynamic="true"]');
    links.forEach(link => {
        const href = link.getAttribute('href');
        if (!allowedUrls.includes(href)) {
            link.remove();
            console.log(`${href} CSS가 제거되었습니다.`);
        }
    });
    // cssRegistry도 갱신 (만약 관리 중인 경우)
    [...cssRegistry.keys()].forEach(url => {
        if (!allowedUrls.includes(url)) {
            cssRegistry.delete(url);
        }
    });
}

