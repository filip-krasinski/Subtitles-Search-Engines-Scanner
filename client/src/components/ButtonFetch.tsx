import React from "react"
import { RiFindReplaceLine } from 'react-icons/ri'
import SubtitlesModel from "../model/SubtitlesModel"
import { hash_napiprojekt, hash_opensubtitles_napisy24 } from "../helpers/Hashes";

interface IProps {
    files: Array<File>
    lang: string
    onFetch: (arg: Array<SubtitlesModel>) => void
}

const API_URL = process.env.REACT_APP_API_URL;

export const ButtonFetch: React.FC<IProps> = ({ files, lang, onFetch }) => {

    const fetchData = async() => {
        for (let i = 0; i < files.length; ++i) {
            const file = files[i];
            const napi  = await hash_napiprojekt(file);
            const op_na = await hash_opensubtitles_napisy24(file);

            const url = `${API_URL}/scan?opensubtitles.org=${op_na}&napisy24.pl=${op_na}&napiprojekt.pl${napi}&fs=${file.size}&ln=${lang}`
            let res =  await fetch(url).then(res => res.json())

            let subs = []
            for (let j = 0; j < res.length; ++j) {
                subs.push(new SubtitlesModel(file.name, res[j].host, res[j].size, res[j].ext, res[j].data))
            }

            onFetch(subs)
        }
    }

    return (
        <>
            <button id="fetch" onClick={fetchData}>
                <RiFindReplaceLine /> find
            </button>
        </>
    )
}