// inputRules.js

export const validationRules = {
    name:{
    },
    nickname: {
    },
    email: {
    },
    'title':{ // 중범주 없는 타이틀
        required: true,
        pattern: /^[a-zA-Z0-9가-힣\s]+$/,
        maxLength: 50,
    },
    'albumName':{
        required: true,
        minLength: 2,
        maxLength:255,
        pattern: /^[a-zA-Z0-9가-힣\s]+$/,
    },
    'description':{
        required: true,
        maxLength: 500,
        pattern: /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!?\s]+$/ // 한글 초성도 됨 , 느낌표 물음표 가능, 공백도 됨
    },
    'albumDTO.albumName':{ // 중범주 포함 앨범네임
        required: true,
        minLength: 2,
        maxLength:255,
        pattern: /^[a-zA-Z0-9가-힣\s]+$/,
    },
    'albumDTO.albumArtPath':{
        required: true,
        minLength:2,
        maxLength:255,
        pattern: /^[a-zA-Z0-9\-!@?._/()\s'"]+$/,
    },
    'albumDTO.description':{
        required: true,
        maxLength: 500,
        pattern: /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!?.,/'_"-()\s]+$/ // 한글 초성도 됨 , 느낌표 물음표 가능, 공백도 됨
    },
    'musicDTO.description':{
        required: true,
        maxLength: 500,
        pattern: /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!?.,/'_"-()\s]+$/ // 한글 초성도 됨 , 느낌표 물음표 가능, 공백도 됨
    },
    'musicDTO.title':{
        required:true,
        minLength: 2,
        maxLength:50,
        pattern: /^[a-zA-Z0-9가-힣\s]+$/, // 한글 초성은 안됨, 공백은 됨
    },
    'musicDTO.price': {
        required:true,
        pattern: /^[0-9]+$/,  // 숫자만 허용 (정수)
    },
    'tagsDTO.instrument':{
        required:true,
        pattern: /^[a-z0-9.,()-_\s]+$/, // 소문자 숫자
    },
    'tagsDTO.mood':{
        required:true,
        pattern: /^[a-z0-9.,()-_\s]+$/, // 소문자 숫자 허용
    },
    'tagsDTO.genre':{
        required:true,
        pattern: /^[a-z0-9.,()-_\s]+$/, // 소문자 숫자 허용
    },
};

export const processingRules = {
    nickname: ['trim', 'toLowerCase'], // 앞뒤 공백 제거, 소문자 변환
    email: ['trim'],                  // 공백 제거

    'title':['trim'],
    'description':['trim'],
    'albumName':['trim'],
    'albumDto.albumName': ['trim', 'toUpperCase'],
    'albumDto.description': ['trim', 'toUpperCase'],
    'musicDto.title': ['trim', 'toUpperCase'],
    'musicDto.price': ['trim'],
    'tagsDto.instrument':['trim', 'toLowerCase'],
    'tagsDto.mood': ['trim', 'toLowerCase'],
    'tagsDto.genre': ['trim', 'toLowerCase'],
    'musicDTO.soundType':['trim','toLowerCase'],
    'albumDTO.verify':['trim'],
};

