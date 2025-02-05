export function formatDate(dateArray) {
    if (!dateArray || !Array.isArray(dateArray)) return 'N/A';
    const [year, month, day, hour, minute, second] = dateArray;
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ` +
        `${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:${String(second).padStart(2, '0')}`;
}