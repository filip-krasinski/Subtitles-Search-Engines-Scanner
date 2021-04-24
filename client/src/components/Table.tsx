import React from 'react';
import { Row } from './Row'
import SubtitlesModel from '../model/SubtitlesModel';

interface IProps {
    subs: Array<SubtitlesModel>
    page: number
    subsPerPage: number
}

export const Table: React.FC<IProps> = ({subs, page , subsPerPage}) => {

    const lastPage = page * subsPerPage
    const firstPage = lastPage - subsPerPage
    const currentSubs = subs.slice(firstPage, lastPage)

    return (
        <>

            <table>
                <thead>
                <tr>
                    <th key='name'>FILE</th>
                    <th key='host'>HOST</th>
                    <th key='size'>SIZE</th>
                    <th key='ext'>EXT</th>
                    <th key='dwn'>DOWNLOAD</th>
                </tr>
                </thead>
                <tbody>
                {currentSubs.map((sub, index) =>
                    <tr key={index} className='active'>
                        <Row subtitles={sub}/>
                    </tr>
                )}
                </tbody>
            </table>

        </>
    )
}