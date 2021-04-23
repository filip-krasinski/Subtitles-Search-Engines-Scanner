export default class SubtitlesModel {

    name: string
    host: string | undefined
    size: number | undefined
    ext:  string | undefined
    data: string | undefined

    constructor(name: string,
                host: string | undefined,
                size: number | undefined,
                ext:  string | undefined,
                data: string | undefined) {
        this.name = name;
        this.host = host;
        this.size = size;
        this.ext = ext;
        this.data = data;
    }

}