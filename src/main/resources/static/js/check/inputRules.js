// inputRules.js

export const validationRules = {
    name:{
        required: true,
        minLength: 2,
        maxLength : 37,
        pattern: /^(?!\s*$)[a-zA-Z가-힣]+$/
    },
    nickname: {
        required: true,
        minLength: 2,
        maxLength: 50,
        pattern: /^(?!\s*$)[a-zA-Z0-9가-힣]+$/
    },
    email: {
        required: true,
        pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    },
    password: {
        required: true,
        minLength: 8,
        pattern: /^(?=.*[!@#$%^&*])(?=.*[A-Z])(?=.*\d)[A-Za-z\d!@#$%^&*]{8,}$/
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
    'description':{
        required: true,
        maxLength: 500,
        pattern: /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!?.,/'_"-()\s]+$/ // 한글 초성도 됨 , 느낌표 물음표 가능, 공백도 됨
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
    'instrument':{
        required:true,
        pattern: /^[a-z0-9.,()-_\s]+$/, // 소문자 숫자
    },
    'mood':{
        required:true,
        pattern: /^[a-z0-9.,()-_\s]+$/, // 소문자 숫자
    },
    'genre':{
        required:true,
        pattern: /^[a-z0-9.,()-_\s]+$/, // 소문자 숫자
    },
    'tagName':{
        required:true,
        pattern: /^[a-z0-9.,()-_\s]+$/, // 소문자 숫자
    }
};

export const processingRules = {
    name: ['trim', 'trimMiddle'],
    nickname: ['trim','trimMiddle'], // 앞뒤 공백 제거, 중간 공백 제거
    email: ['trim'],                  // 공백 제거
    password: ['trim', 'trimMiddle'],
    title:['trim'],
    description:['trim'],
    albumName:['trim'],
    'albumDto.albumName': ['trim', 'toUpperCase'],
    'albumDto.description': ['trim', 'toUpperCase'],
    'musicDto.title': ['trim', 'toUpperCase'],
    'musicDto.price': ['trim'],
    'tagsDto.instrument':['trim', 'toLowerCase'],
    'tagsDto.mood': ['trim', 'toLowerCase'],
    'tagsDto.genre': ['trim', 'toLowerCase'],
    'musicDTO.soundType':['trim','toLowerCase'],
    'albumDTO.verify':['trim'],
    instrument:['trim','toLowerCase'],
    mood:['trim','toLowerCase'],
    genre:['trim','toLowerCase'],
    tagName:['trim','toLowerCase'],
};

