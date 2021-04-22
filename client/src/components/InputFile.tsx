import React, { ChangeEvent, createRef } from 'react'
import { GiCloudUpload } from 'react-icons/gi'

interface IProps {
    onInput: (files: Array<File>) => void;
}

export const InputFile: React.FC<IProps> = ({onInput}) => {
    const spanRef = createRef<any>()

    const onChange = (e: ChangeEvent<HTMLInputElement>) => {
        const target = e.target;
        const files = target.files;
        let cont = target.getAttribute('data-default-caption');

        if (files) {
            if (files.length > 1)
                cont = target.getAttribute('data-multiple-caption')!
                    .replace('{count}', String(files.length));
            else if (files.length === 1)
                cont = target.value.split('\\').pop()!;
        }

        spanRef.current.innerHTML = cont
        onInput(files ? Array.from(files) : [])
    }

    return (
        <>
            <input
                type='file'
                className='button-basic inputfile'
                id='file'
                onChange={(e) => onChange(e)}
                data-multiple-caption='{count} files selected'
                data-default-caption='Choose files&hellip;'
                accept='video/*, .mkv'
                multiple
            />
            <label htmlFor='file'>
                <GiCloudUpload/>
                <span ref={spanRef}>Choose files&hellip;</span>
            </label>
        </>
    )
}