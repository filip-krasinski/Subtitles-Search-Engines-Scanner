export default class SubtitlesModel {

    name: string
    host: string
    size: number
    ext: string
    data: string


    constructor(name: string, host: string, size: number, ext: string, data: string) {
        this.name = name;
        this.host = host;
        this.size = size;
        this.ext = ext;
        this.data = data;
    }

}