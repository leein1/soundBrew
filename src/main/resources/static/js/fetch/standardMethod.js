// standardMethod.js
import { fetchData } from '/js/fetch/standardAxios.js';

export const get = async (endpoint, params = {}) => fetchData(endpoint, 'GET', null, false, params);
export const post = async (endpoint, body, params = {}) => fetchData(endpoint, 'POST', body, true, params);
export const put = async (endpoint, body, params = {}) => fetchData(endpoint, 'PUT', body, true, params);
export const deleteRequest = async (endpoint, params = {}) => fetchData(endpoint, 'DELETE', null, true, params);

