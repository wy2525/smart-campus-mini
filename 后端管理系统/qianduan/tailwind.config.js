/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#409EFF',
          light: '#66b1ff',
          dark: '#337ecc',
        },
        success: {
          DEFAULT: '#67C23A',
          light: '#85ce61',
          dark: '#529b2e',
        },
        warning: {
          DEFAULT: '#E6A23C',
          light: '#ebb563',
          dark: '#b88230',
        },
        danger: {
          DEFAULT: '#F56C6C',
          light: '#f78989',
          dark: '#c45656',
        },
        info: {
          DEFAULT: '#909399',
          light: '#a6a9ad',
          dark: '#73767a',
        },
      },
    },
  },
  plugins: [],
}
