export class Post {
    constructor(
        public id: number,
        public title: string,
        public url: string,
        public author: string,
        public likes: number,
        public liked: boolean
    ) {}
}
