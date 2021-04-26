// eslint-disable-next-line @typescript-eslint/no-var-requires
const StyleLintPlugin = require("stylelint-webpack-plugin");

module.exports = {
  configureWebpack: {
    plugins: [
      // ホットリロード時に stylelint を実行する
      new StyleLintPlugin({
        files: ["src/**/*.{vue,scss}"],
      }),
    ],
  },
};
