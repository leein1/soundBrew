// inputRules.js

export const validationRules = {
    name:{
    },
    nickname: {
    },
    email: {
    },
    'albumDto.albumName':{
        required: true,
        minLength: 2,
        maxLength:50,
        pattern: /^[a-zA-Z0-9가-힣\s]+$/, // 한글 초성은 안됨, 공백은 됨
    },
    'title':{
        required: true,
        pattern: /^[a-zA-Z0-9가-힣\s]+$/,
        maxLength: 10,
    },
    'albumDto.description':{
        required: true,
        maxLength: 500,
        pattern: /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!?\s]+$/ // 한글 초성도 됨 , 느낌표 물음표 가능, 공백도 됨
    },
    'musicDto.title':{
        required:true,
        minLength: 2,
        maxLength:50,
        pattern: /^[a-zA-Z0-9가-힣\s]+$/, // 한글 초성은 안됨, 공백은 됨
    },
    'musicDto.price': {
        required:true,
        pattern: /^[0-9]+$/,  // 숫자만 허용 (정수)
    },
    'tagsDto.instrument':{
        required:true,
        pattern: /^[a-z0-9]+$/, // 소문자 숫자
    },
    'tagsDto.mood':{
        required:true,
        pattern: /^[a-z0-9]+$/, // 소문자 숫자 허용
    },
    'tagsDto.genre':{
        required:true,
        pattern: /^[a-z0-9]+$/, // 소문자 숫자 허용
    },
};

export const processingRules = {
    nickname: ['trim', 'toLowerCase'], // 앞뒤 공백 제거, 소문자 변환
    email: ['trim'],                  // 공백 제거

    'albumDto.albumName': ['trim', 'toUpperCase'],
    'albumDto.description': ['trim', 'toUpperCase'],
    'musicDto.title': ['trim', 'toUpperCase'],
    'musicDto.price': ['trim'],
    'tagsDto.instrument':['trim', 'toLowerCase', 'toUpperCase'],
    'tagsDto.mood': ['trim', 'toLowerCase', 'toUpperCase'],
    'tagsDto.genre': ['trim', 'toLowerCase', 'toUpperCase'],
};

