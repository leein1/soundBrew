export function loadExternalCSS(url) {
    return new Promise((resolve, reject) => {
        if (document.querySelector(`link[href="${url}"]`)) {
            resolve();
            return;
        }
        const link = document.createElement("link");
        link.rel = "stylesheet";
        link.href = url;
        link.type = "text/css";
        link.onload = () => resolve();
        link.onerror = () => reject(new Error(`Failed to load CSS: ${url}`));
        document.head.appendChild(link);
    });
}

export function removeExternalCSS(url) {
    const link = document.querySelector(`link[href="${url}"]`);
    if (link) {
        link.remove();
        console.log(`${url} CSS가 제거되었습니다.`);
    } else {
        console.log(`${url} CSS가 존재하지 않습니다.`);
    }
}

const ATypeCSSFiles = [
    "/css/sound/music.css",
    "/css/sound/album.css",
    "/css/sound/sound.css",
    "/css/sound/album-list.css"
];

const BTypeCSSFiles = [
    "/css/sound/manage-main.css",
    "/css/sound/manage-albums.css",
    "/css/sound/manage-tags.css",
    "/css/sound/music-upload.css"
];

export function removeATypeCSS() {
    ATypeCSSFiles.forEach(removeExternalCSS);
}

export function removeBTypeCSS() {
    BTypeCSSFiles.forEach(removeExternalCSS);
}

export async function loadATypeCSS() {
    await Promise.all(ATypeCSSFiles.map(url => loadExternalCSS(url)));
}

export async function loadBTypeCSS() {
    await Promise.all(BTypeCSSFiles.map(url => loadExternalCSS(url)));
}
