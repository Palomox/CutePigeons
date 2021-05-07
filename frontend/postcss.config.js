module.exports = {
    // Add you postcss configuration here
    // Learn more about it at https://github.com/webpack-contrib/postcss-loader#config-files
    plugins: [
        require('postcss-import'),
        require('tailwindcss'),
        require('autoprefixer'),
        require('cssnano')({
            preset: ['default', {
                discardComments: {
                    removeAll: true,
                },
            }],
        }),
    ]
};
